package ai.instance.tiamatStrongHold;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.controllers.observer.DeathObserver;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.GeneralNpcAI;

/**
 * @author Estrayl
 */
@AIName("captured_drakan_scientist")
public class CapturedDrakanScientistAI extends GeneralNpcAI {

	private final AtomicInteger deadGuardingEyes = new AtomicInteger();
	private final AtomicBoolean isActivated = new AtomicBoolean();

	public CapturedDrakanScientistAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleCreatureSee(Creature creature) {
		if (creature instanceof Player && isActivated.compareAndSet(false, true)) {
			getKnownList().forEachNpc(n -> {
				if (n.getNpcId() == 219390 && PositionUtil.isInRange(getOwner(), n, 25)) {
					n.getObserveController().attach(new DeathObserver(_ -> handleObservedNpcDied()));
				}
			});
		}
	}

	private void handleObservedNpcDied() {
		if (deadGuardingEyes.incrementAndGet() >= 2)
			ThreadPoolManager.getInstance().schedule(this::startWalk, Rnd.get(10, 20) * 100); // NPCs will start walking after some delay
	}

	private void startWalk() {
		setStateIfNot(AIState.WALKING);
		getOwner().setState(CreatureState.ACTIVE, true);
		getMoveController().moveToPoint(838, 1317, 396);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getOwner().getObjectId()));
		ThreadPoolManager.getInstance().schedule(this::handleNpcEscaping, 9000);
	}

	private void handleNpcEscaping() {
		handleQuestUpdate();
		AIActions.deleteOwner(this);
	}

	private void handleQuestUpdate() {
		for (Player player : getPosition().getWorldMapInstance().getPlayersInside())
			updateQuestEntryIfPossible(player);
	}

	private void updateQuestEntryIfPossible(Player player) {
		int quest = player.getRace().equals(Race.ELYOS) ? 30708 : 30758;
		final QuestState qs = player.getQuestStateList().getQuestState(quest);
		if (qs != null) {
			synchronized (qs) {
				if (qs.getQuestVarById(0) != 5) {
					qs.setQuestVar(qs.getQuestVarById(0) + 1);
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(SM_QUEST_ACTION.ActionType.UPDATE, qs));
				}
			}
		}
	}
}
