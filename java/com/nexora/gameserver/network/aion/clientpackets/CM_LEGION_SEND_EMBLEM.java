package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.team.legion.Legion;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.LegionService;

/**
 * @author Simple, cura, Neon
 */
public class CM_LEGION_SEND_EMBLEM extends AionClientPacket {

	private int legionId;

	public CM_LEGION_SEND_EMBLEM(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		legionId = readD();
	}

	@Override
	protected void runImpl() {
		Legion legion = LegionService.getInstance().getLegion(legionId);
		if (legion != null)
			LegionService.getInstance().sendEmblemData(getConnection().getActivePlayer(), legion.getLegionEmblem(), legionId, legion.getName());
	}
}
