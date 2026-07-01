package com.nexora.gameserver.network.aion.clientpackets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.BrokerService;
import com.nexora.gameserver.utils.audit.AuditLogger;

/**
 * @author namedrisk
 */
public class CM_BROKER_SEARCH extends AionClientPacket {

	private int brokerObjId;
	private byte sortType;
	private int page;
	private int mask;
	private List<Integer> itemList;

	public CM_BROKER_SEARCH(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		brokerObjId = readD();
		sortType = readC(); // 1 - name; 2 - level; 4 - totalPrice; 6 - price for piece
		page = readUH();
		mask = readUH();
		int itemCount = readUH();
		itemList = new ArrayList<>(itemCount);
		for (int index = 0; index < itemCount; index++)
			itemList.add(readD());
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.isTargetingNpcWithFunction(brokerObjId, DialogAction.OPEN_VENDOR))
			BrokerService.getInstance().showRequestedItems(player, mask, sortType, page, itemList);
		else
			AuditLogger.log(player, "tried to search for items in broker without targeting a broker");
	}
}
