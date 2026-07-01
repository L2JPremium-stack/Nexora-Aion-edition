package com.nexora.gameserver.network.chatserver.clientpackets;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_CHAT_INIT;
import com.nexora.gameserver.network.chatserver.ChatServer;
import com.nexora.gameserver.network.chatserver.CsClientPacket;
import com.nexora.gameserver.services.ban.ChatBanService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.World;

/**
 * @author ATracer
 */
public class CM_CS_PLAYER_AUTH_RESPONSE extends CsClientPacket {

	/**
	 * Player for which authentication was performed
	 */
	private int playerId;
	/**
	 * Token will be sent to client
	 */
	private byte[] token;

	/**
	 * @param opcode
	 */
	public CM_CS_PLAYER_AUTH_RESPONSE(int opcode) {
		super(opcode);
	}

	@Override
	protected void readImpl() {
		playerId = readD();
		int tokenLenght = readUC();
		token = readB(tokenLenght);
	}

	@Override
	protected void runImpl() {
		if (ChatServer.getInstance().isUp()) {
			Player player = World.getInstance().getPlayer(playerId);
			if (player != null) {
				PacketSendUtility.sendPacket(player, new SM_CHAT_INIT(token));
				if (ChatBanService.isBanned(player))
					ChatServer.getInstance().sendPlayerGagPacket(player.getObjectId(), ChatBanService.getBanMinutes(player) * 60000L);
			}
		}
	}
}
