package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.mail.MailService;

/**
 * @author kosyachok
 */
public class CM_GET_MAIL_ATTACHMENT extends AionClientPacket {

	private int mailObjId;
	private byte attachmentType;

	public CM_GET_MAIL_ATTACHMENT(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		mailObjId = readD();
		attachmentType = readC(); // 0 - item , 1 - kinah
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		MailService.getAttachments(player, mailObjId, attachmentType);
	}
}
