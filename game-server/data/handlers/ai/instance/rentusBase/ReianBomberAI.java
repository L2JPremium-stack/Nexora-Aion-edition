package ai.instance.rentusBase;

import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.ai.AISubState;
import com.nexora.gameserver.ai.manager.WalkManager;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.world.WorldMapInstance;
import com.nexora.gameserver.world.WorldPosition;

import ai.GeneralNpcAI;

/**
 * @author xTz
 */
@AIName("reian_bomber")
public class ReianBomberAI extends GeneralNpcAI {

	private AtomicBoolean hasArrivedBoss = new AtomicBoolean(false);
	private int position = 1;

	public ReianBomberAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		getSpawnTemplate().setWalkerId("30028000024");
		WalkManager.startWalking(this);
		getOwner().setState(CreatureState.ACTIVE, true);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
	}

	@Override
	protected void handleMoveArrived() {
		int point = getOwner().getMoveController().getCurrentStep().getStepIndex();
		super.handleMoveArrived();
		if (hasArrivedBoss.get()) {
			startHelpEvent();
		} else if (point == 7 && hasArrivedBoss.compareAndSet(false, true)) {
			getSpawnTemplate().setWalkerId(null);
			WalkManager.stopWalking(this);
			startHelpEvent();
		}
	}

	private void startHelpEvent() {
		getMoveController().abortMove();
		setStateIfNot(AIState.IDLE);
		setSubStateIfNot(AISubState.NONE);
		SkillEngine.getInstance().getSkill(getOwner(), 19374, 60, getOwner()).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isDead()) {
					setSubStateIfNot(AISubState.WALK_RANDOM);
					setStateIfNot(AIState.WALKING);
					switch (position) {
						case 1:
							help(359.763f, 585.597f, 145.525f);
							getMoveController().moveToPoint(346.47787f, 604.0337f, 145.8766f);
							position++;
							break;
						case 2:
							help(346.086f, 597.062f, 146.119f);
							getMoveController().moveToPoint(370.93597f, 607.6427f, 145.41916f);
							position++;
							break;
						case 3:
							help(362.143f, 604.723f, 146.125f);
							getMoveController().moveToPoint(361.7722f, 584.4937f, 145.63573f);
							position = 1;
							break;
					}
				}
			}
		}, 8000);

	}

	private void deleteNpc(Npc npc) {
		if (npc != null) {
			npc.getController().delete();
		}
	}

	private void help(float x, float y, float z) {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		if (instance != null) {
			for (Npc npc : instance.getNpcs(282530)) {
				WorldPosition p = npc.getPosition();
				if (p.getX() == x && p.getY() == y) {
					deleteNpc(npc);
				}
			}
			for (Npc npc : instance.getNpcs(282387)) {
				WorldPosition p = npc.getPosition();
				if (p.getX() == x && p.getY() == y) {
					return;
				}
			}
			Npc npc = (Npc) spawn(282387, x, y, z, (byte) 0);
			SkillEngine.getInstance().getSkill(npc, 19731, 1, npc).useNoAnimationSkill();
		}
	}

}
