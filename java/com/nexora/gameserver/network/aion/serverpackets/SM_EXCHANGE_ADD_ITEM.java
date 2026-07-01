package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.item.ItemTemplate;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;
import com.nexora.gameserver.network.aion.iteminfo.ItemInfoBlob;

/**
 * @author Avol, ATracer
 */
public class SM_EXCHANGE_ADD_ITEM extends AionServerPacket {

	private Player player;
	private int action;
	private Item item;

	public SM_EXCHANGE_ADD_ITEM(int action, Item item, Player player) {
		this.player = player;
		this.action = action;
		this.item = item;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		ItemTemplate itemTemplate = item.getItemTemplate();

		writeC(action); // 0 -self 1-other

		writeD(itemTemplate.getTemplateId());
		writeD(item.getObjectId());
		writeS(itemTemplate.getL10n());

		ItemInfoBlob itemInfoBlob = ItemInfoBlob.getFullBlob(player, item);
		itemInfoBlob.writeMe(getBuf());
	}
}
