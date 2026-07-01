package ai.instance.tiamatStrongHold;

import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.world.WorldMapInstance;

import ai.GeneralNpcAI;

/**
 * @author Cheatkiller
 */
@AIName("muragan")
public class MuraganAI extends GeneralNpcAI {

	private final AtomicBoolean isInMove = new AtomicBoolean(false);

	public MuraganAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		if (getOwner().getNpcId() == 800438) {
			PacketSendUtility.broadcastMessage(getOwner(), 390852, 1000);
		}
	}

	@Override
	protected void handleCreatureSee(Creature creature) {
		checkDistance(creature);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		checkDistance(creature);
	}

	private void checkDistance(Creature creature) {
		if (creature instanceof Player && PositionUtil.isInRange(getOwner(), creature, 15) && isInMove.compareAndSet(false, true)) {
			openSuramaDoor();
			startWalk();
		}
	}

	private void startWalk() {
		int owner = getNpcId();
		if (owner == 800436 || owner == 800438)
			return;
		if (owner == 800435) {
			PacketSendUtility.broadcastMessage(getOwner(), 390837);
			PacketSendUtility.broadcastMessage(getOwner(), 390838, 4000);
			killGuardCaptain();
		}
		setStateIfNot(AIState.WALKING);
		getOwner().setState(CreatureState.ACTIVE, true);
		getMoveController().moveToPoint(838, 1317, 396);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getOwner().getObjectId()));
		ThreadPoolManager.getInstance().schedule(() -> AIActions.deleteOwner(MuraganAI.this), 10000);
	}

	private void openSuramaDoor() {
		if (getOwner().getNpcId() == 800436) {
			PacketSendUtility.broadcastMessage(getOwner(), 390835);
			getPosition().getWorldMapInstance().setDoorState(56, true);
			AIActions.deleteOwner(this);
		}
	}

	private void killGuardCaptain() {
		WorldMapInstance instance = getOwner().getPosition().getWorldMapInstance();
		for (Npc npc : instance.getNpcs(219392)) {
			spawn(283145, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());// 4.0
			npc.getController().delete();
		}
	}
}
