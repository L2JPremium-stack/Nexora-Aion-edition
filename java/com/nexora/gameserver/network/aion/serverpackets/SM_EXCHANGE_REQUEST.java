package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author -Avol-
 */
public class SM_EXCHANGE_REQUEST extends AionServerPacket {

	private String receiver;

	public SM_EXCHANGE_REQUEST(String receiver) {
		this.receiver = receiver;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeS(receiver);
	}
}
