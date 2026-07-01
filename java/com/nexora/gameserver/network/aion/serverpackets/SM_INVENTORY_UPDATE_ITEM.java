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
 * @author ATracer, -Nemesiss-
 */
public class SM_INVENTORY_UPDATE_ITEM extends AionServerPacket {

	private final Player player;
	private final Item item;
	private final ItemUpdateType updateType;

	public SM_INVENTORY_UPDATE_ITEM(Player player, Item item) {
		this(player, item, ItemUpdateType.DEC_ITEM_USE);
	}

	public SM_INVENTORY_UPDATE_ITEM(Player player, Item item, ItemUpdateType updateType) {
		this.player = player;
		this.item = item;
		this.updateType = updateType;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		ItemTemplate itemTemplate = item.getItemTemplate();

		writeD(item.getObjectId());
		writeS(itemTemplate.getL10n());

		ItemInfoBlob itemInfoBlob;
		switch (updateType) {
			case EQUIP_UNEQUIP:
				itemInfoBlob = new ItemInfoBlob(player, item);
				itemInfoBlob.addBlobEntry(ItemBlobType.EQUIPPED_SLOT);
				break;
			case CHARGE:
				itemInfoBlob = new ItemInfoBlob(player, item);
				itemInfoBlob.addBlobEntry(ItemBlobType.CONDITIONING_INFO);
				break;
			case POLISH_CHARGE:
				itemInfoBlob = new ItemInfoBlob(player, item);
				itemInfoBlob.addBlobEntry(ItemBlobType.POLISH_INFO);
				break;
			default:
				itemInfoBlob = ItemInfoBlob.getFullBlob(player, item);
				break;
		}
		itemInfoBlob.writeMe(getBuf());

		if (updateType.isSendable())
			writeH(updateType.getMask());
	}
}
