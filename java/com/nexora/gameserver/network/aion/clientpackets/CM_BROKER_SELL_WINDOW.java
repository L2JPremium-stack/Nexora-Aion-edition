package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.BrokerService;

/**
 * @author ginho1
 */
public class CM_BROKER_SELL_WINDOW extends AionClientPacket {

	private int itemUniqueId;

	public CM_BROKER_SELL_WINDOW(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		this.itemUniqueId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		if (player.isTrading())
			return;

		BrokerService.getInstance().showSellWindow(player, itemUniqueId);
	}
}
