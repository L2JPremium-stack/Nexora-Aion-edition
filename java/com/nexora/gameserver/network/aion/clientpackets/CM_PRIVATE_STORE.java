package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.trade.TradePSItem;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.PrivateStoreService;

/**
 * @author Simple
 */
public class CM_PRIVATE_STORE extends AionClientPacket {

	private TradePSItem[] tradePSItems;

	public CM_PRIVATE_STORE(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		int itemCount = readUH();
		tradePSItems = new TradePSItem[itemCount];
		for (int i = 0; i < itemCount; i++) {
			int itemObjId = readD();
			int itemId = readD();
			int count = readUH();
			long price = readQ();
			tradePSItems[i] = new TradePSItem(itemObjId, itemId, count, price);
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (tradePSItems.length <= 0)
			PrivateStoreService.closePrivateStore(player);
		else
			PrivateStoreService.createStoreWithItems(player, tradePSItems);
	}
}
