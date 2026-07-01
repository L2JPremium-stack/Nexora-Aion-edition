package com.nexora.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.item.ItemTemplate;
import com.nexora.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.nexora.gameserver.network.aion.serverpackets.SM_MEGAPHONE;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Rolandas, ginho1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MegaphoneAction")
public class MegaphoneAction extends AbstractItemAction {

	@XmlAttribute(name = "color")
	protected String color;

	public int getColor() {
		int rgb = Integer.parseInt(color, 16);
		return rgb;
	}

	@Override
	public boolean canAct(Player player, Item item, Item targetItem, Object... params) {
		return true;
	}

	@Override
	public void act(Player player, Item item, Item targetItem, Object... params) {
		String message = (String) params[0];
		ItemTemplate itemTemplate = item.getItemTemplate();
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), itemTemplate.getTemplateId()),
			true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_USE_ITEM(item.getL10n()));
		player.getInventory().decreaseByObjectId(item.getObjectId(), 1);
		PacketSendUtility.broadcastToWorld(new SM_MEGAPHONE(player, message, item.getItemId()));
	}
}
