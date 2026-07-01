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
public class CM_BROKER_CANCEL_REGISTERED extends AionClientPacket {

	private int brokerObjId;
	private int brokerItemId;

	public CM_BROKER_CANCEL_REGISTERED(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		brokerObjId = readD();
		brokerItemId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.isTargetingNpcWithFunction(brokerObjId, DialogAction.OPEN_VENDOR))
			BrokerService.getInstance().cancelRegisteredItem(player, brokerItemId);
		else
			AuditLogger.log(player, "tried to unregister his registered broker item without targeting a broker");
	}
}
