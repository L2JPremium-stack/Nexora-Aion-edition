package ai.instance.nightmareCircus;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.AggressiveNpcAI;

/**
 * @author Ritsu
 */
@AIName("solidironchain")
public class SolidIronChainAI extends AggressiveNpcAI {

	public SolidIronChainAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		PacketSendUtility.broadcastToMap(getOwner(), new SM_PLAY_MOVIE(false, 0, 0, 983, true));
	}

}
