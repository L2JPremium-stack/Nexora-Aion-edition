package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Rolandas
 */
public class SM_DELETE_HOUSE_OBJECT extends AionServerPacket {

	private int itemObjectId;

	public SM_DELETE_HOUSE_OBJECT(int itemObjectId) {
		this.itemObjectId = itemObjectId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(itemObjectId);
	}

}
