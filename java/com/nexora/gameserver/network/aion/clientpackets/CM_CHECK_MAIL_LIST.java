package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.mail.MailService;

/**
 * @author ginho1
 */
public class CM_CHECK_MAIL_LIST extends AionClientPacket {

	public boolean expressOnly;

	public CM_CHECK_MAIL_LIST(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		expressOnly = readC() == 1;
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player != null)
			MailService.sendMailList(player, expressOnly, false);
	}

}
