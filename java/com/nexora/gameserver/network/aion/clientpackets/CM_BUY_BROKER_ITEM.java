package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.BrokerService;
import com.nexora.gameserver.utils.audit.AuditLogger;

/**
 * @author kosyak
 */
public class CM_BUY_BROKER_ITEM extends AionClientPacket {

	private int brokerObjId;
	private int itemUniqueId;
	private long itemCount;

	public CM_BUY_BROKER_ITEM(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		brokerObjId = readD();
		itemUniqueId = readD();
		itemCount = readQ();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (itemCount < 1)
			return;
		if (player.isTargetingNpcWithFunction(brokerObjId, DialogAction.OPEN_VENDOR))
			BrokerService.getInstance().buyBrokerItem(player, itemUniqueId, itemCount);
		else
			AuditLogger.log(player, "tried to buy an item from broker without targeting a broker");
	}
}
