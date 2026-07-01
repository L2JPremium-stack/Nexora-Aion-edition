package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Rolandas
 */
public class SM_HOUSE_TELEPORT extends AionServerPacket {

	int address;
	int playerId;

	public SM_HOUSE_TELEPORT(int houseAddress, int playerId) {
		this.address = houseAddress;
		this.playerId = playerId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(address);
		writeD(playerId);
	}
}
