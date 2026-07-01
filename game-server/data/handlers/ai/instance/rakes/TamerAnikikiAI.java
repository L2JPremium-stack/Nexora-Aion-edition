package ai.instance.rakes;

import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.manager.WalkManager;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.GeneralNpcAI;

/**
 * @author xTz
 */
@AIName("tamer_anikiki")
public class TamerAnikikiAI extends GeneralNpcAI {

	private AtomicBoolean isStartedWalkEvent = new AtomicBoolean(false);

	public TamerAnikikiAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		super.handleCreatureMoved(creature);
		if (getNpcId() == 219040 && isInRange(creature, 10) && creature instanceof Player) {
			if (isStartedWalkEvent.compareAndSet(false, true)) {
				getSpawnTemplate().setWalkerId("3004600001");
				WalkManager.startWalking(this);
				getOwner().setState(CreatureState.ACTIVE, true);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
				// Key Box
				spawn(700553, 611, 481, 936, (byte) 90);
				spawn(700553, 657, 482, 936, (byte) 60);
				spawn(700553, 626, 540, 936, (byte) 1);
				spawn(700553, 645, 534, 936, (byte) 75);
				PacketSendUtility.sendPacket((Player) creature, new SM_QUEST_ACTION(0, 180));
				PacketSendUtility.broadcastToMap(getOwner(), 1400262);
			}
		}
	}

	@Override
	protected void handleMoveArrived() {
		if (getNpcId() == 219040) {
			int point = getOwner().getMoveController().getCurrentStep().getStepIndex();
			if (point == 12) {
				getSpawnTemplate().setWalkerId(null);
				WalkManager.stopWalking(this);
				AIActions.deleteOwner(this);
				return;
			} else if (point == 8) {
				super.handleMoveArrived();
				getOwner().setState(CreatureState.WALK_MODE, true);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
				return;
			}
		}
		super.handleMoveArrived();
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		if (getNpcId() != 219040) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					SkillEngine.getInstance().getSkill(getOwner(), 18189, 20, getOwner()).useNoAnimationSkill();
				}

			}, 5000);
		}
	}

	@Override
	public float modifyDamage(Creature attacker, float damage, Effect effect) {
		if (getNpcId() == 219037)
			return damage;
		else
			return 1;
	}
}
