package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Equipment;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.nexora.gameserver.restrictions.PlayerRestrictions;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Avol, ATracer
 */
public class CM_EQUIP_ITEM extends AionClientPacket {

	private long slotRead;
	private int itemObjId;
	private byte action;

	public CM_EQUIP_ITEM(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		action = readC(); // 0/1/2 = equip/unequip/switch weapons
		slotRead = readQ();
		itemObjId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activePlayer = getConnection().getActivePlayer();

		activePlayer.getController().cancelUseItem();

		if (!PlayerRestrictions.canChangeEquip(activePlayer))
			return;

		Equipment equipment = activePlayer.getEquipment();
		Item resultItem = null;
		switch (action) {
			case 0:
				resultItem = equipment.equipItem(itemObjId, slotRead);
				break;
			case 1:
				resultItem = equipment.unEquipItem(itemObjId);
				if (resultItem == null)
					PacketSendUtility.sendPacket(activePlayer, SM_SYSTEM_MESSAGE.STR_UI_INVENTORY_FULL());
				break;
			case 2:
				equipment.switchHands();
				break;
		}

		if (resultItem != null || action == 2)
			PacketSendUtility.broadcastPacket(activePlayer,
				new SM_UPDATE_PLAYER_APPEARANCE(activePlayer.getObjectId(), equipment.getEquippedForAppearance()), true);
	}
}
