package ai.instance.shugoImperialTomb;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.AggressiveNpcAI;

/**
 * @author Ritsu
 */
@AIName("shugo_tomb_ghost")
public class ShugoTombGhostAI extends AggressiveNpcAI {

	public ShugoTombGhostAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		getOwner().setState(CreatureState.ACTIVE, true);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
	}

	@Override
	public boolean ask(AIQuestion question) {
		return switch (question) {
			case REWARD_LOOT, ALLOW_DECAY -> switch (getNpcId()) {
				case 219505, 219506, 219507 -> false;
				default -> true;
			};
			default -> super.ask(question);
		};
	}
}
