package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.BrokerService;
import com.nexora.gameserver.utils.audit.AuditLogger;

/**
 * @author kosyachok
 */
public class CM_BROKER_LIST extends AionClientPacket {

	private int brokerObjId;
	private byte sortType;
	private int page;
	private int listMask;

	public CM_BROKER_LIST(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		brokerObjId = readD();
		sortType = readC(); // 1 - name; 2 - level; 4 - totalPrice; 6 - price for piece
		page = readUH();
		listMask = readUH();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.isTargetingNpcWithFunction(brokerObjId, DialogAction.OPEN_VENDOR))
			BrokerService.getInstance().showRequestedItems(player, listMask, sortType, page, null);
		else
			AuditLogger.log(player, "tried to browse for broker items without targeting a broker");
	}
}
