package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Rolandas
 */
public class SM_PACKAGE_INFO_NOTIFY extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(1);
		writeC(3);
		writeD(0); // time until pack expiration
	}

}
