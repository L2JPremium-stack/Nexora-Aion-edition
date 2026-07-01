package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Rolandas
 */
public class SM_FRIEND_STATUS extends AionServerPacket {

	int status;

	public SM_FRIEND_STATUS(int status) {
		this.status = status;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(status);
	}

}
