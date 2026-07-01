package com.nexora.gameserver.model.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.instance.instancescore.HarmonyArenaScore;
import com.nexora.gameserver.model.instance.instancescore.InstanceScore;
import com.nexora.gameserver.model.instance.instancescore.PvPArenaScore;
import com.nexora.gameserver.model.stats.calc.StatOwner;
import com.nexora.gameserver.model.stats.calc.functions.IStatFunction;
import com.nexora.gameserver.model.stats.calc.functions.StatAddFunction;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.model.templates.instance_bonusatrr.InstanceBonusAttr;
import com.nexora.gameserver.model.templates.instance_bonusatrr.InstancePenaltyAttr;
import com.nexora.gameserver.network.aion.instanceinfo.ArenaScoreWriter;
import com.nexora.gameserver.network.aion.instanceinfo.HarmonyScoreWriter;
import com.nexora.gameserver.network.aion.serverpackets.SM_ABNORMAL_STATE;
import com.nexora.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.nexora.gameserver.skillengine.change.Func;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.world.WorldMapInstance;

/**
 * @author xTz
 */
public class InstanceBuff implements StatOwner {

	private final List<IStatFunction> functions = new ArrayList<>();
	private final InstanceBonusAttr instanceBonusAttr;
	private Future<?> task;
	private long endTime;

	public InstanceBuff(int buffId) {
		instanceBonusAttr = DataManager.INSTANCE_BUFF_DATA.getInstanceBonusattr(buffId);
	}

	public void applyEffect(Player player, int time) {

		if (isActive() || instanceBonusAttr == null) {
			return;
		}
		if (time != 0) {
			task = ThreadPoolManager.getInstance().schedule(new InstanceBuffTask(player), time);
		}
		endTime = System.currentTimeMillis() + time;
		for (InstancePenaltyAttr instancePenaltyAttr : instanceBonusAttr.getPenaltyAttr()) {
			StatEnum stat = instancePenaltyAttr.getStat();
			int statToModified = player.getGameStats().getStat(stat, 0).getBase();
			int value = instancePenaltyAttr.getValue();
			int valueModified = instancePenaltyAttr.getFunc().equals(Func.PERCENT) ? (statToModified * value / 100) : (value);
			functions.add(new StatAddFunction(stat, valueModified, true));
		}
		player.getGameStats().addEffect(this, functions);
	}

	public void endEffect(Player player) {
		functions.clear();
		if (isActive()) {
			task.cancel(true);
		}
		player.getGameStats().endEffect(this);
		notify(player);
	}

	private void notify(Player player) {
		WorldMapInstance wmi = player.getWorldMapInstance();
		InstanceScore<?> score = wmi.getInstanceHandler().getInstanceScore();
		if (score instanceof HarmonyArenaScore harmonyScore) {
			wmi.forEachPlayer(p -> PacketSendUtility.sendPacket(p, new SM_INSTANCE_SCORE(wmi.getMapId(),
				new HarmonyScoreWriter(harmonyScore, InstanceScoreType.UPDATE_PLAYER_BUFF_STATUS, player), harmonyScore.getTime())));
		} else if (score instanceof PvPArenaScore arenaScore) {
			wmi.forEachPlayer(
				p -> PacketSendUtility.sendPacket(p, new SM_INSTANCE_SCORE(wmi.getMapId(), new ArenaScoreWriter(arenaScore, p.getObjectId(), false))));
		}
		PacketSendUtility.sendPacket(player, new SM_ABNORMAL_STATE(Collections.emptyList(), player.getEffectController().getAbnormals(), 0));
	}

	public int getRemainingTime() {
		return (int) Math.max(0, endTime - System.currentTimeMillis());
	}

	private class InstanceBuffTask implements Runnable {

		private final Player player;

		public InstanceBuffTask(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
			endEffect(player);
		}

	}

	public boolean isActive() {
		return task != null && !task.isDone();
	}

}
