package com.nexora.loginserver.network.gameserver.clientpackets;

import com.nexora.loginserver.network.gameserver.GsClientPacket;

/**
 * @author KID
 */
public class CM_GS_PONG extends GsClientPacket {

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		getConnection().getPingPongTask().onReceivePong();
	}
}
