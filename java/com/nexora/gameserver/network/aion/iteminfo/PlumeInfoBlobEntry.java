package com.nexora.gameserver.network.aion.iteminfo;

import java.nio.ByteBuffer;

import com.nexora.gameserver.model.items.ItemSlot;
import com.nexora.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

/**
 * @author Rolandas
 */
public class PlumeInfoBlobEntry extends ItemBlobEntry {

	PlumeInfoBlobEntry() {
		super(ItemBlobType.PLUME_INFO);
	}

	@Override
	public void writeThisBlob(ByteBuffer buf) {
		writeQ(buf, ItemSlot.getSlotFor(ownerItem.getItemTemplate().getItemSlot()).getSlotIdMask());
		writeQ(buf, 0x100000); // secondary slot ?
		writeD(buf, 0); // unks
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
	}

	@Override
	public int getSize() {
		return 32;
	}

}
