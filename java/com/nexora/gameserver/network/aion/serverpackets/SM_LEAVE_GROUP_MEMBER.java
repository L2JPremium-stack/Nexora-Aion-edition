package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Lyahim
 */
public class SM_LEAVE_GROUP_MEMBER extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {

		writeD(0x00);
		writeC(0x00);
		writeD(0x3F); // TODO: TeamType.getType
		writeD(0x00); // TODO: TeamType.getSubType
		writeH(0x00);
	}
}
