package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.house.House;
import com.nexora.gameserver.network.aion.AionConnection;

/**
 * @author Rolandas, Neon
 */
public class SM_HOUSE_UPDATE extends AbstractHouseInfoPacket {

	public SM_HOUSE_UPDATE(House house) {
		super(house);
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(1); // unk
		writeH(0);
		writeH(1); // unk (if this is 0 any changed house settings are ignored on client side)

		writeCommonInfo();
	}
}
