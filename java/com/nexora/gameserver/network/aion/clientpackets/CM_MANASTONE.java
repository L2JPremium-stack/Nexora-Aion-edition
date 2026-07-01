package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.item.actions.EnchantItemAction;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.EnchantService;
import com.nexora.gameserver.services.StigmaService;
import com.nexora.gameserver.services.item.ItemSocketService;
import com.nexora.gameserver.utils.PositionUtil;

/**
 * @author ATracer, Wakizashi
 */
public class CM_MANASTONE extends AionClientPacket {

	private int npcObjId;
	private int slotNum;
	private int actionType;
	private int targetFusedSlot;
	private int stoneUniqueId;
	private int targetItemUniqueId;
	private int supplementUniqueId;

	/**
	 * @param opcode
	 */
	public CM_MANASTONE(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		actionType = readUC();
		targetFusedSlot = readUC();
		targetItemUniqueId = readD();
		switch (actionType) {
			case 1:
			case 2:
			case 4:
			case 8:
				stoneUniqueId = readD();
				supplementUniqueId = readD();
				break;
			case 3:
				slotNum = readUC();
				readC();
				readH();
				npcObjId = readD();
				break;
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		switch (actionType) {
			case 1: // enchant stone
			case 2: // add manastone
				Item stone = player.getInventory().getItemByObjId(stoneUniqueId);
				if (stone == null)
					return;
				Item targetItem = player.getEquipment().getEquippedItemByObjId(targetItemUniqueId);
				if (targetItem == null && (targetItem = player.getInventory().getItemByObjId(targetItemUniqueId)) == null) {
					sendPacket(actionType == 1 ? SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_NO_TARGET_ITEM() : SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTION_NO_TARGET_ITEM());
					return;
				}

				if (stone.getItemTemplate().isStigma() && targetItem.getItemTemplate().isStigma()) {
					StigmaService.chargeStigma(player, targetItem, stone);
				} else {
					EnchantItemAction action = new EnchantItemAction();
					if (action.canAct(player, stone, targetItem)) {
						Item supplement = player.getInventory().getItemByObjId(supplementUniqueId);
						if (supplement != null) {
							if (supplement.getItemId() / 100000 != 1661) // suppliment id check
								return;
						}
						action.act(player, stone, targetItem, supplement, targetFusedSlot);
					}
				}
				break;
			case 3: // remove manastone
				VisibleObject visibleObject = player.getTarget();
				if (visibleObject.getObjectId() == npcObjId && visibleObject instanceof Npc npc && PositionUtil.isInTalkRange(player, npc))
					ItemSocketService.removeManastone(player, targetItemUniqueId, slotNum, targetFusedSlot != 1);
				break;
			case 4: // add godstone
				Item weaponItem = player.getInventory().getItemByObjId(targetItemUniqueId);
				if (weaponItem == null) {
					boolean isEquipped = player.getEquipment().getEquippedItemByObjId(targetItemUniqueId) != null;
					sendPacket(isEquipped ? SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_PROC_CANNOT_GIVE_PROC_TO_EQUIPPED_ITEM() : SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_PROC_NO_TARGET_ITEM());
					return;
				}
				ItemSocketService.socketGodstone(player, weaponItem, stoneUniqueId);
				break;
			case 8: // amplification
				EnchantService.amplifyItem(player, targetItemUniqueId, supplementUniqueId, stoneUniqueId);
				break;
		}
	}

}
