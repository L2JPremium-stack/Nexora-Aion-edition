package com.nexora.gameserver.network.loginserver.clientpackets;

import com.nexora.gameserver.dao.PlayerDAO;
import com.nexora.gameserver.network.loginserver.LsClientPacket;
import com.nexora.gameserver.network.loginserver.serverpackets.SM_GS_CHARACTER;

/**
 * @author cura
 */
public class CM_GS_CHARACTER_RESPONSE extends LsClientPacket {

	public CM_GS_CHARACTER_RESPONSE(int opCode) {
		super(opCode);
	}

	private int accountId;

	@Override
	public void readImpl() {
		accountId = readD();
	}

	@Override
	public void runImpl() {
		int characterCount = PlayerDAO.getCharacterCountOnAccount(accountId);
		sendPacket(new SM_GS_CHARACTER(accountId, characterCount));
	}
}
