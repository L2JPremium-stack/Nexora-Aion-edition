package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * Tells the client that auction related data has changed. The client will send CM_GET_HOUSE_BIDS if needed.
 * 
 * @author Rolandas
 */
public class SM_RECEIVE_BIDS extends AionServerPacket {

	private final int unk;

	public SM_RECEIVE_BIDS(int unk) {
		this.unk = unk;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(unk);
	}

}
