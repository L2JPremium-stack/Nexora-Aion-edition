package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Sweetkr
 */
public class SM_TARGET_UPDATE extends AionServerPacket {

	private Player player;

	public SM_TARGET_UPDATE(Player player) {
		this.player = player;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(player.getObjectId());
		writeD(player.getTarget() == null ? 0 : player.getTarget().getObjectId());
	}
}
