package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author xavier
 */
public class SM_UPDATE_NOTE extends AionServerPacket {

	private final int targetObjId;
	private final String note;

	public SM_UPDATE_NOTE(Player player) {
		this.targetObjId = player.getObjectId();
		this.note = player.getCommonData().getNote();
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(targetObjId);
		writeS(note);
	}
}
