package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author -Nemesiss-
 */
public class SM_KEY extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(con.enableCryptKey());
	}
}
