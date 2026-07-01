package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

public class SM_GAMEGUARD extends AionServerPacket {

	private int size;

	public SM_GAMEGUARD(int size) {
		this.size = size;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(size);
		writeB(new byte[size]);
	}
}
