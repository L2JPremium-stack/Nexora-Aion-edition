package com.nexora.gameserver.network.chatserver.serverpackets;

import com.nexora.gameserver.configs.network.NetworkConfig;
import com.nexora.gameserver.network.chatserver.ChatServerConnection;
import com.nexora.gameserver.network.chatserver.CsServerPacket;

/**
 * @author ATracer, Neon
 */
public class SM_CS_AUTH extends CsServerPacket {

	public SM_CS_AUTH() {
		super(0x00);
	}

	@Override
	protected void writeImpl(ChatServerConnection con) {
		writeC(NetworkConfig.GAMESERVER_ID);
		writeS(NetworkConfig.CHAT_PASSWORD);
	}
}
