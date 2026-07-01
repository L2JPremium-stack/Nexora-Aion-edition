package com.nexora.gameserver.network.aion.clientpackets;

import java.util.List;
import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.house.HouseBids;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_HOUSE_BIDS;
import com.nexora.gameserver.services.HousingBidService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.collections.DynamicServerPacketBodySplitList;
import com.nexora.gameserver.utils.collections.SplitList;

/**
 * @author Rolandas
 */
public class CM_GET_HOUSE_BIDS extends AionClientPacket {

	public CM_GET_HOUSE_BIDS(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {

	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		List<HouseBids> houseBids = HousingBidService.getInstance().getBidInfo(player.getRace());
		SplitList<HouseBids> bidsSplitList = new DynamicServerPacketBodySplitList<>(houseBids, true, SM_HOUSE_BIDS.STATIC_BODY_SIZE,
			SM_HOUSE_BIDS.DYNAMIC_BODY_PART_SIZE_CALCULATOR);
		bidsSplitList.forEach(part -> PacketSendUtility.sendPacket(player, new SM_HOUSE_BIDS(part.isFirst(), part.isLast(), part)));
	}

}
