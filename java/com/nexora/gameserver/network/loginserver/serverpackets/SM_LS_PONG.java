package com.nexora.gameserver.network.loginserver.serverpackets;

import com.nexora.gameserver.network.loginserver.LoginServerConnection;
import com.nexora.gameserver.network.loginserver.LsServerPacket;

/**
 * @author KID
 */
public class SM_LS_PONG extends LsServerPacket {

	public SM_LS_PONG() {
		super(12);
	}

	@Override
	protected void writeImpl(LoginServerConnection con) {
	}
}
