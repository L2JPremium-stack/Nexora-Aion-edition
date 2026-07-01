package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.utils.audit.AuditLogger;

/**
 * @author ginho1
 */
public class CM_CHECK_PAK extends AionClientPacket {

	@SuppressWarnings("unused")
	private byte unk;
	private String pakStatus;

	public CM_CHECK_PAK(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		unk = readC(); // 2
		pakStatus = readS();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (!pakStatus.isEmpty() && !pakStatus.endsWith("[1:OK]") && !pakStatus.contains("File not found"))
			AuditLogger.log(player, "using modified data pak: " + pakStatus);
	}
}
