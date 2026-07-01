package com.nexora.loginserver.network.aion.clientpackets;

import java.nio.ByteBuffer;

import com.nexora.loginserver.GameServerInfo;
import com.nexora.loginserver.GameServerTable;
import com.nexora.loginserver.network.aion.AionAuthResponse;
import com.nexora.loginserver.network.aion.AionClientPacket;
import com.nexora.loginserver.network.aion.LoginConnection;
import com.nexora.loginserver.network.aion.SessionKey;
import com.nexora.loginserver.network.aion.serverpackets.SM_LOGIN_FAIL;
import com.nexora.loginserver.network.aion.serverpackets.SM_PLAY_FAIL;
import com.nexora.loginserver.network.aion.serverpackets.SM_PLAY_OK;

/**
 * @author -Nemesiss-
 */
public class CM_PLAY extends AionClientPacket {

	/**
	 * accountId is part of session key - its used for security purposes
	 */
	private int accountId;
	/**
	 * loginOk is part of session key - its used for security purposes
	 */
	private int loginOk;
	/**
	 * Id of game server that this client is trying to play on.
	 */
	private byte servId;

	public CM_PLAY(ByteBuffer buf, LoginConnection client, int opCode) {
		super(buf, client, opCode);
	}

	@Override
	protected void readImpl() {
		accountId = readD();
		loginOk = readD();
		servId = readC();
		readB(6); // CE 15 F9 75 78 30 or all zero
		readQ(); // random
	}

	@Override
	protected void runImpl() {
		LoginConnection con = getConnection();
		SessionKey key = con.getSessionKey();
		if (key.checkLogin(accountId, loginOk)) {
			GameServerInfo gsi = GameServerTable.getGameServerInfo(servId);
			if (gsi == null || !gsi.isOnline())
				con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.STR_L2AUTH_S_SERVER_DOWN));
			else if (gsi.getMinAccessLevel() > con.getAccount().getAccessLevel())
				con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.STR_L2AUTH_S_SEVER_CHECK));
			else if (gsi.isFull())
				con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.STR_L2AUTH_S_LIMIT_EXCEED));
			else {
				con.setJoinedGs();
				sendPacket(new SM_PLAY_OK(key, servId));
			}
		} else
			con.close(new SM_LOGIN_FAIL(AionAuthResponse.STR_L2AUTH_S_SYSTEM_ERROR));
	}
}
