package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.item.ItemSplitService;

/**
 * @author kosyak
 */
public class CM_SPLIT_ITEM extends AionClientPacket {

	int sourceItemObjId;
	byte sourceStorageType;
	long itemAmount;
	int destinationItemObjId;
	byte destinationStorageType;
	short slotNum;

	public CM_SPLIT_ITEM(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		sourceItemObjId = readD();
		itemAmount = readQ();
		sourceStorageType = readC();
		destinationItemObjId = readD();
		destinationStorageType = readC();
		slotNum = readH();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		ItemSplitService.splitItem(player, sourceItemObjId, destinationItemObjId, itemAmount, slotNum, sourceStorageType, destinationStorageType);
	}
}
