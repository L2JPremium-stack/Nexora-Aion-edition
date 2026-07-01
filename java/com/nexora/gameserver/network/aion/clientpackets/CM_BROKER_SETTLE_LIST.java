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
public class CM_BROKER_SETTLE_LIST extends AionClientPacket {

	private int brokerObjId, startPageIndex;

	public CM_BROKER_SETTLE_LIST(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		brokerObjId = readD();
		startPageIndex = readUH();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.isTargetingNpcWithFunction(brokerObjId, DialogAction.OPEN_VENDOR))
			BrokerService.getInstance().showSettledItems(player, startPageIndex);
		else
			AuditLogger.log(player, "tried to open the broker sold item list without targeting a broker");
	}
}
