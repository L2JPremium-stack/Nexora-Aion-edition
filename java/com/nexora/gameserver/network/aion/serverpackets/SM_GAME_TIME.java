package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;
import com.nexora.gameserver.services.GameTimeService;

/**
 * Sends the current time in the server in minutes since 1/1/00 00:00:00
 * 
 * @author Ben
 */
public class SM_GAME_TIME extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(GameTimeService.getInstance().getGameTime().getTime()); // Minutes since 1/1/00 00:00:00
	}
}
