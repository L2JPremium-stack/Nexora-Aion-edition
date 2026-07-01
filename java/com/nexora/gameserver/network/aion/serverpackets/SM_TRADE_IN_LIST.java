package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.templates.tradelist.TradeListTemplate;
import com.nexora.gameserver.model.templates.tradelist.TradeListTemplate.TradeTab;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author MrPoke
 */
public class SM_TRADE_IN_LIST extends AionServerPacket {

	private Npc npc;
	private TradeListTemplate tlist;
	private int buyPriceModifier;

	public SM_TRADE_IN_LIST(Npc npc, TradeListTemplate tlist, int buyPriceModifier) {
		this.npc = npc;
		this.tlist = tlist;
		this.buyPriceModifier = buyPriceModifier;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		if ((tlist != null) && (tlist.getNpcId() != 0) && (tlist.getCount() != 0)) {
			writeD(npc.getObjectId());
			writeC(tlist.getTradeNpcType().index());
			writeD(buyPriceModifier); // Vendor Buy Price Modifier
			writeD(100);// new aion 4.5
			writeH(tlist.getCount());
			for (TradeTab tradeTabl : tlist.getTradeTablist()) {
				writeD(tradeTabl.getId());
			}
		}
	}
}
