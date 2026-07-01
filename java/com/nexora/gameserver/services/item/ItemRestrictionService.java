package com.nexora.gameserver.services.item;

import com.nexora.gameserver.configs.main.LegionConfig;
import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.items.storage.StorageType;
import com.nexora.gameserver.model.team.legion.LegionPermissionsMask;
import com.nexora.gameserver.model.templates.item.ItemTemplate;
import com.nexora.gameserver.model.templates.item.enums.ItemGroup;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class ItemRestrictionService {

	/**
	 * Check if item can be moved from storage by player
	 */
	public static boolean isItemRestrictedFrom(Player player, Item item, StorageType storageType) {
		switch (storageType) {
			case LEGION_WAREHOUSE:
				if (!LegionConfig.LEGION_WAREHOUSE || !player.isLegionMember() || !player.getLegionMember().hasRights(LegionPermissionsMask.WH_WITHDRAWAL)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_WAREHOUSE_NO_RIGHT());
					return true;
				}
				break;
		}
		return false;
	}

	/**
	 * Check if item can be moved to storage by player
	 */
	public static boolean isItemRestrictedTo(Player player, Item item, StorageType storageType) {
		switch (storageType) {
			case REGULAR_WAREHOUSE:
				if (!item.isStorableInWarehouse()) {
					// You cannot store this in the warehouse.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WAREHOUSE_CANT_DEPOSIT_ITEM());
					return true;
				}
				break;
			case ACCOUNT_WAREHOUSE:
				if (!item.isStorableInAccWarehouse()) {
					// You cannot store this item in the account warehouse.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WAREHOUSE_CANT_ACCOUNT_DEPOSIT());
					return true;
				}
				break;
			case LEGION_WAREHOUSE:
				if (!item.isStorableInLegWarehouse() || !LegionConfig.LEGION_WAREHOUSE) {
					// You cannot store this item in the Legion warehouse.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WAREHOUSE_CANT_LEGION_DEPOSIT());
					return true;
				} else if (!player.isLegionMember() || (!player.getLegionMember().hasRights(LegionPermissionsMask.WH_DEPOSIT)
					&& !player.getLegionMember().hasRights(LegionPermissionsMask.WH_WITHDRAWAL))) {
					// You do not have the authority to use the Legion warehouse.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_WAREHOUSE_NO_RIGHT());
					return true;
				}
				break;
		}

		return false;
	}

	/** Check, whether the item can be removed */
	public static boolean canRemoveItem(Player player, Item item) {
		ItemTemplate it = item.getItemTemplate();
		if (it.getItemGroup().equals(ItemGroup.QUEST)) {
			// TODO: not removable, if quest status start and quest can not be abandoned
			// Waiting for quest data reparse
			return true;
		}
		return true;
	}

}
