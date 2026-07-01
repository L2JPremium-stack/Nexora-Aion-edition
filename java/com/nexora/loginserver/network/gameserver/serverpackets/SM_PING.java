package com.nexora.loginserver.network.gameserver.serverpackets;

import com.nexora.loginserver.network.gameserver.GsConnection;
import com.nexora.loginserver.network.gameserver.GsServerPacket;

/**
 * @author KID
 */
public class SM_PING extends GsServerPacket {

	@Override
	protected void writeImpl(GsConnection con) {
		writeC(11);
	}
}
