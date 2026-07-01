package com.nexora.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.item.ItemTemplate;
import com.nexora.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.nexora.gameserver.services.CubeExpandService;
import com.nexora.gameserver.services.WarehouseService;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExpandInventoryAction")
public class ExpandInventoryAction extends AbstractItemAction {

	@XmlAttribute(name = "level")
	private int level;
	@XmlAttribute(name = "storage")
	private StorageType storage;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem, Object... params) {
		switch (storage) {
			case CUBE:
				return CubeExpandService.canExpandByTicket(player, level);
			case WAREHOUSE:
				return WarehouseService.canExpandByTicket(player, level);
		}
		return false;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem, Object... params) {
		if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1))
			return;
		ItemTemplate itemTemplate = parentItem.getItemTemplate();
		PacketSendUtility.broadcastPacket(player,
			new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), itemTemplate.getTemplateId()), true);

		switch (storage) {
			case CUBE:
				CubeExpandService.itemExpand(player);
				break;
			case WAREHOUSE:
				WarehouseService.expand(player, false);
				break;
		}
	}

}

enum StorageType {
	CUBE,
	WAREHOUSE
}
