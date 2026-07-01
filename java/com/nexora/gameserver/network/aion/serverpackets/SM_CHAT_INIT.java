package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer
 */
public class SM_CHAT_INIT extends AionServerPacket {

	private byte[] token;

	/**
	 * @param token
	 */
	public SM_CHAT_INIT(byte[] token) {
		this.token = token;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(token.length);
		writeB(token);
	}
}
