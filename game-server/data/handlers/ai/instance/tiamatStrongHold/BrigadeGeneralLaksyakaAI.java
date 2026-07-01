package ai.instance.tiamatStrongHold;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.CreatureType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI;

/**
 * @author Cheatkiller
 */
@AIName("brigadegenerallaksyaka")
public class BrigadeGeneralLaksyakaAI extends AggressiveNpcAI {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	private Future<?> skeletonTask;
	private boolean isFinalBuff;

	public BrigadeGeneralLaksyakaAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (Rnd.chance() < 3)
			spawnSummon();
		if (isHome.compareAndSet(true, false))
			startSkillTask();
		if (!isFinalBuff && getOwner().getLifeStats().getHpPercentage() <= 25) {
			isFinalBuff = true;
			AIActions.useSkill(this, 20731);
		}
	}

	private void startSkillTask() {
		skeletonTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isDead())
					cancelTask();
				else {
					startSkeletonEvent();
				}
			}
		}, 5000, 40000);
	}

	private void cancelTask() {
		if (skeletonTask != null && !skeletonTask.isCancelled()) {
			skeletonTask.cancel(true);
		}
	}

	private void startSkeletonEvent() {
		Npc tiamatEye = getPosition().getWorldMapInstance().getNpc(283089);// 4.0
		List<Player> players = new ArrayList<>();
		getKnownList().forEachPlayer(player -> {
			if (!player.isDead() && PositionUtil.isInRange(player, tiamatEye, 40)) {
				players.add(player);
			}
		});
		if (!players.isEmpty()) {
			Player player = Rnd.get(players);
			SkillEngine.getInstance().applyEffectDirectly(20865, tiamatEye, player);
		}
	}

	private void spawnSummon() {
		if (getPosition().getWorldMapInstance().getNpc(283115) == null) {// 4.0
			rndSpawn(283115, 4);// 4.0
		}
	}

	private void rndSpawn(int npcId, int count) {
		for (int i = 0; i < count; i++) {
			rndSpawnInRange(npcId, 10);
		}
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		cancelTask();
	}

	@Override
	protected void handleBeforeSpawned() {
		super.handleBeforeSpawned();
		getOwner().overrideNpcType(CreatureType.PEACE);
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelTask();
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		cancelTask();
		isFinalBuff = false;
		isHome.set(true);
	}
}
