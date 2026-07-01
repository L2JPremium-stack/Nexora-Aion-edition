package com.nexora.loginserver.network.gameserver.clientpackets;

import org.slf4j.LoggerFactory;

import com.nexora.commons.utils.Rnd;
import com.nexora.loginserver.controller.AccountController;
import com.nexora.loginserver.model.Account;
import com.nexora.loginserver.model.ReconnectingAccount;
import com.nexora.loginserver.network.gameserver.GsClientPacket;
import com.nexora.loginserver.network.gameserver.serverpackets.SM_ACCOUNT_RECONNECT_KEY;

/**
 * This packet is sent by GameServer when player is requesting fast reconnect to login server. LoginServer in response will send reconectKey.
 * 
 * @author -Nemesiss-
 */
public class CM_ACCOUNT_RECONNECT_KEY extends GsClientPacket {

	/**
	 * accountId of account that will be reconnecting.
	 */
	private int accountId;

	@Override
	protected void readImpl() {
		accountId = readD();
	}

	@Override
	protected void runImpl() {
		int reconectKey = Rnd.nextInt();
		Account acc = getConnection().getGameServerInfo().removeAccountFromGameServer(accountId);
		if (acc == null)
			LoggerFactory.getLogger(CM_ACCOUNT_RECONNECT_KEY.class).warn(getConnection() + " requested reconnection for account " + accountId + ", but account is not registered on game server");
		else
			AccountController.addReconnectingAccount(new ReconnectingAccount(acc, reconectKey));
		sendPacket(new SM_ACCOUNT_RECONNECT_KEY(accountId, reconectKey));
	}
}
