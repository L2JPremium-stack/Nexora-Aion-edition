package com.nexora.loginserver.network.aion.serverpackets;

import com.nexora.loginserver.network.aion.AionAuthResponse;
import com.nexora.loginserver.network.aion.AionServerPacket;
import com.nexora.loginserver.network.aion.LoginConnection;

/**
 * Sent to notify the client that he was kicked from loginserver
 * 
 * @author Neon
 */
public final class SM_ACCOUNT_KICK extends AionServerPacket {

	private final int msgId;

	public SM_ACCOUNT_KICK(AionAuthResponse msg) {
		super(0x08);
		this.msgId = msg.getId();
	}

	@Override
	protected void writeImpl(LoginConnection con) {
		writeD(msgId); // reason
	}
}
