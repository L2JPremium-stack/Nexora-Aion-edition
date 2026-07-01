package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author xTz
 */
public class SM_MARK_FRIENDLIST extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(con.getActivePlayer().getObjectId());
		writeC(1);
		writeH(0);
	}

}
