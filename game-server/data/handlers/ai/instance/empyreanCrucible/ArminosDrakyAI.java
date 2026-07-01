package ai.instance.empyreanCrucible;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.manager.WalkManager;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI;

/**
 * @author xTz
 */
@AIName("arminos_draky")
public class ArminosDrakyAI extends GeneralNpcAI {

	private String walkerId = "300300001";
	private boolean isStart = true;

	public ArminosDrakyAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		getSpawnTemplate().setWalkerId(walkerId);
		WalkManager.startWalking(this);
		getOwner().setState(CreatureState.ACTIVE, true);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
	}

	@Override
	protected void handleMoveArrived() {
		super.handleMoveArrived();
		if (getOwner().getMoveController().getCurrentStep().isLastStep()) { // circle twice
			if (!isStart) {
				getSpawnTemplate().setWalkerId(null);
				WalkManager.stopWalking(this);
				AIActions.deleteOwner(this);
			} else
				isStart = false;
		}
	}

	@Override
	public boolean ask(AIQuestion question) {
		return switch (question) {
			case IS_IMMUNE_TO_ABNORMAL_STATES, REWARD_AP -> true;
			default -> super.ask(question);
		};
	}

	@Override
	public boolean canThink() {
		return false;
	}
}
