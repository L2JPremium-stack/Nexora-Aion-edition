package com.nexora.loginserver.network.gameserver.clientpackets;

import com.nexora.loginserver.controller.AccountTimeController;
import com.nexora.loginserver.model.Account;
import com.nexora.loginserver.network.gameserver.GsClientPacket;

/**
 * In this packet GameServer is informing LoginServer that some account is no longer on GameServer [ie was disconencted]
 * 
 * @author -Nemesiss-
 */
public class CM_ACCOUNT_DISCONNECTED extends GsClientPacket {

	/**
	 * AccountId of account that was disconnected form GameServer.
	 */
	private int accountId;

	@Override
	protected void readImpl() {
		accountId = readD();
	}

	@Override
	protected void runImpl() {
		Account account = getConnection().getGameServerInfo().removeAccountFromGameServer(accountId);

		/**
		 * account can be null if a player logged out from gs {@link CM_ACCOUNT_RECONNECT_KEY}
		 */
		if (account != null) {
			AccountTimeController.updateOnLogout(account);
		}
	}
}
