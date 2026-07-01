package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.spawns.SpawnSearchResult;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_SHOW_NPC_ON_MAP;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;

/**
 * @author Lyahim
 */
public class CM_OBJECT_SEARCH extends AionClientPacket {

	private int npcId;

	/**
	 * Constructs new client packet instance.
	 * 
	 * @param opcode
	 */
	public CM_OBJECT_SEARCH(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		this.npcId = readD();
	}

	@Override
	protected void runImpl() {
		Player activePlayer = getConnection().getActivePlayer();
		if (activePlayer == null) {
			return;
		}
		SpawnSearchResult searchResult = DataManager.SPAWNS_DATA.getNearestSpawnByNpcId(activePlayer, npcId, activePlayer.getWorldId());
		if (searchResult != null)
			sendPacket(new SM_SHOW_NPC_ON_MAP(activePlayer, npcId, searchResult.getWorldId(), searchResult.getSpot().getX(), searchResult.getSpot().getY(),
				searchResult.getSpot().getZ()));
		else
			sendPacket(SM_SYSTEM_MESSAGE.STR_FIND_POS_UNKNOWN_NAME());
	}
}
