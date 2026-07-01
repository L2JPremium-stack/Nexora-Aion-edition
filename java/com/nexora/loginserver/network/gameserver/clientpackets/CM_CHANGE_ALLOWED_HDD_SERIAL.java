package com.nexora.loginserver.network.gameserver.clientpackets;

import com.nexora.loginserver.dao.AccountDAO;
import com.nexora.loginserver.network.gameserver.GsClientPacket;

public class CM_CHANGE_ALLOWED_HDD_SERIAL extends GsClientPacket {

	private int accountId;
	private String hddSerial;

	protected void readImpl() {
		accountId = readD();
		hddSerial = readS();
	}

	protected void runImpl() {
		AccountDAO.updateAllowedHDDSerial(accountId, hddSerial);
	}
}
