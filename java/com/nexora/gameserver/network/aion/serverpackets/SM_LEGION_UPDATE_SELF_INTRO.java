package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Simple
 */
public class SM_LEGION_UPDATE_SELF_INTRO extends AionServerPacket {

	private String selfintro;
	private int playerObjId;

	public SM_LEGION_UPDATE_SELF_INTRO(int playerObjId, String selfintro) {
		this.selfintro = selfintro;
		this.playerObjId = playerObjId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(playerObjId);
		writeS(selfintro);
	}
}
