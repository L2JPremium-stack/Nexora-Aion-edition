package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.item.ItemTemplate;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;
import com.nexora.gameserver.network.aion.iteminfo.ItemInfoBlob;
import com.nexora.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;
import com.nexora.gameserver.services.item.ItemPacketService.ItemUpdateType;

/**
 * @author kosyachok, -Nemesiss-
 */
public class SM_WAREHOUSE_UPDATE_ITEM extends AionServerPacket {

	private Player player;
	private Item item;
	private int warehouseType;
	private ItemUpdateType updateType;

	public SM_WAREHOUSE_UPDATE_ITEM(Player player, Item item, int warehouseType, ItemUpdateType updateType) {
		this.player = player;
		this.item = item;
		this.warehouseType = warehouseType;
		this.updateType = updateType;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		ItemTemplate itemTemplate = item.getItemTemplate();

		writeD(item.getObjectId());
		writeC(warehouseType);
		writeS(itemTemplate.getL10n());

		ItemInfoBlob itemInfoBlob = new ItemInfoBlob(player, item);
		itemInfoBlob.addBlobEntry(ItemBlobType.GENERAL_INFO);
		itemInfoBlob.writeMe(getBuf());

		if (updateType.isSendable())
			writeH(updateType.getMask());
	}
}
