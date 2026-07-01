package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

public class SM_PONG extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(0x00);
		writeC(0x00);
	}
}
