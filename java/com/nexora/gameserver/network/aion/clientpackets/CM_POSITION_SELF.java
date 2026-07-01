package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.serverpackets.SM_POSITION_SELF;

/**
 * Client sends this in response to {@link SM_POSITION_SELF}
 */
public class CM_POSITION_SELF extends AionClientPacket {

	public CM_POSITION_SELF(int opcode, Set<AionConnection.State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
	}
}
