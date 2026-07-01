package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Simple
 */
public class SM_LEGION_UPDATE_NICKNAME extends AionServerPacket {

	private int playerObjId;
	private String newNickname;

	public SM_LEGION_UPDATE_NICKNAME(int playerObjId, String newNickname) {
		this.playerObjId = playerObjId;
		this.newNickname = newNickname;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(playerObjId);
		writeS(newNickname);
	}
}
