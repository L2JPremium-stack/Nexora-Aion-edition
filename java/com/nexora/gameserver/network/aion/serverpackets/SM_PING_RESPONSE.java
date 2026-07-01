package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author dragoon112
 */
public class SM_PING_RESPONSE extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(0x04);
	}
}
