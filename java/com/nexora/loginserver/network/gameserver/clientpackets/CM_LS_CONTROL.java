package com.nexora.loginserver.network.gameserver.clientpackets;

import com.nexora.loginserver.dao.AccountDAO;
import com.nexora.loginserver.model.Account;
import com.nexora.loginserver.network.gameserver.GsClientPacket;
import com.nexora.loginserver.network.gameserver.serverpackets.SM_LS_CONTROL_RESPONSE;

/**
 * @author Aionchs-Wylovech
 */
public class CM_LS_CONTROL extends GsClientPacket {

	private byte type, param;
	private int accountId, adminId;

	@Override
	protected void readImpl() {
		type = readC();
		param = readC();
		accountId = readD();
		adminId = readD();
	}

	@Override
	protected void runImpl() {
		Account account = AccountDAO.getAccount(accountId);
		switch (type) {
			case 1 -> account.setAccessLevel(param);
			case 2 -> account.setMembership(param);
		}
		boolean result = AccountDAO.updateAccount(account);
		sendPacket(new SM_LS_CONTROL_RESPONSE(type, param, account.getId(), adminId, result));
	}
}
