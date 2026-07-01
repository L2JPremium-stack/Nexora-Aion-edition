package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;
import com.nexora.gameserver.services.item.ItemPacketService.ItemDeleteType;

//Author Avol

public class SM_DELETE_ITEM extends AionServerPacket {

	private final int itemObjectId;
	private final ItemDeleteType deleteType;

	public SM_DELETE_ITEM(int itemObjectId) {
		this(itemObjectId, ItemDeleteType.DEFAULT);
	}

	public SM_DELETE_ITEM(int itemObjectId, ItemDeleteType deleteType) {
		this.itemObjectId = itemObjectId;
		this.deleteType = deleteType;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(itemObjectId);
		writeC(deleteType.getMask());
	}
}
