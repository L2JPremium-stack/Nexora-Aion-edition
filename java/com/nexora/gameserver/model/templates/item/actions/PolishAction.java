package com.nexora.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.controllers.observer.ItemUseObserver;
import com.nexora.gameserver.dao.ItemStoneListDAO;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.Persistable.PersistentState;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.items.IdianStone;
import com.nexora.gameserver.model.templates.item.bonuses.StatBonusType;
import com.nexora.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.nexora.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolishAction")
public class PolishAction extends AbstractItemAction {

	@XmlAttribute(name = "set_id")
	private int polishSetId;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem, Object... params) {
		if (parentItem.getItemTemplate().getLevel() > targetItem.getItemTemplate().getLevel()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_POLISH_WRONG_LEVEL());
			return false;
		}
		if (!targetItem.isIdentified()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_POLISH_NEED_IDENTIFY());
			return false;
		}
		return !player.isInAttackMode() && targetItem.getItemTemplate().isWeapon() && targetItem.getItemTemplate().isCanPolish();
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem, Object... params) {
		PacketSendUtility.broadcastPacket(player,
			new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 5000, 0, 0), true);
		ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED());
				PacketSendUtility.broadcastPacket(player,
					new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, 2, 0), true);
				player.getObserveController().removeObserver(this);
			}

		};
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(() -> {
			player.getObserveController().removeObserver(observer);

			PacketSendUtility.broadcastPacket(player,
				new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, 1, 1), true);
			if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
				return;
			}
			int bonusNumber = DataManager.ITEM_RANDOM_BONUSES.selectRandomBonusNumber(StatBonusType.POLISH, polishSetId);
			if (bonusNumber == 0) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_FAILED(parentItem.getL10n()));
				return;
			}
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_POLISH_SUCCEED(targetItem.getL10n()));
			IdianStone idianStone = targetItem.getIdianStone();
			if (idianStone != null) {
				idianStone.onUnEquip(player);
				targetItem.setIdianStone(null);
				idianStone.setPersistentState(PersistentState.DELETED);
				ItemStoneListDAO.storeIdianStones(idianStone);
			}
			idianStone = new IdianStone(parentItem.getItemId(), PersistentState.NEW, targetItem, bonusNumber, 1000000);
			targetItem.setIdianStone(idianStone);
			if (targetItem.isEquipped()) {
				idianStone.onEquip(player, targetItem.getEquipmentSlot());
			}
			PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
		}, 5000));

	}

	public int getPolishSetId() {
		return polishSetId;
	}

}
