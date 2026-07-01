package com.nexora.loginserver.network.gameserver.clientpackets;

import com.nexora.loginserver.controller.AccountController;
import com.nexora.loginserver.network.gameserver.GsClientPacket;

/**
 * @author cura
 */
public class CM_GS_CHARACTER extends GsClientPacket {

	private int accountId;
	private int characterCount;

	@Override
	protected void readImpl() {
		accountId = readD();
		characterCount = readUC();
	}

	@Override
	protected void runImpl() {
		AccountController.addGSCharacterCountFor(accountId, getConnection().getGameServerInfo().getId(), characterCount);

		if (AccountController.hasAllGSCharacterCounts(accountId))
			AccountController.sendServerListFor(accountId);
	}
}
