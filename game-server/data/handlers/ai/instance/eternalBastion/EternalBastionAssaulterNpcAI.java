package ai.instance.eternalBastion;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.manager.WalkManager;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Cheatkiller, Estrayl
 */
@AIName("eternal_bastion_assaulter")
public class EternalBastionAssaulterNpcAI extends EternalBastionAggressiveNpcAI {

	public EternalBastionAssaulterNpcAI(Npc owner) {
		super(owner);
	}

	private int getAggroRange() {
		int range = getOwner().getAggroRange();
		if (range < 11)
			range = 11;
		return range;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(this::startWalking, 3000);
	}

	private void startWalking() {
		WalkManager.startWalking(this);
		getOwner().unsetState(CreatureState.WALK_MODE);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
	}

	@Override
	public boolean isDestinationReached() {
		getOwner().unsetState(CreatureState.WALK_MODE);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
		hateCommander();
		return super.isDestinationReached();
	}

	private void hateCommander() {
		Npc commander = getPosition().getWorldMapInstance().getNpc(209516);
		if (commander == null)
			commander = getPosition().getWorldMapInstance().getNpc(209517);
		if (commander != null && !getOwner().getAggroList().isHating(commander) && PositionUtil.isInRange(getOwner(), commander, 20))
			getOwner().getAggroList().addHate(commander, 100000);
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		hateCommander();
	}
}
