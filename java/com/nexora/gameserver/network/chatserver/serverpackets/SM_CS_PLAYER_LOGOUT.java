package com.nexora.gameserver.network.chatserver.serverpackets;

import com.nexora.gameserver.network.chatserver.ChatServerConnection;
import com.nexora.gameserver.network.chatserver.CsServerPacket;

/**
 * @author ATracer
 */
public class SM_CS_PLAYER_LOGOUT extends CsServerPacket {

	private int playerId;

	public SM_CS_PLAYER_LOGOUT(int playerId) {
		super(0x02);
		this.playerId = playerId;
	}

	@Override
	protected void writeImpl(ChatServerConnection con) {
		writeD(playerId);
	}
}
