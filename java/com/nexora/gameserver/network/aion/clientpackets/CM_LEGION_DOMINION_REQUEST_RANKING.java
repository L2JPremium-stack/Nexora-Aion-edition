package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.legionDominion.LegionDominionLocation;
import com.nexora.gameserver.model.team.legion.Legion;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_LEGION_DOMINION_RANK;
import com.nexora.gameserver.services.LegionDominionService;

/**
 * @author Yeats
 */
public class CM_LEGION_DOMINION_REQUEST_RANKING extends AionClientPacket {

	int stonespearId;

	public CM_LEGION_DOMINION_REQUEST_RANKING(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		stonespearId = readD();
	}

	@Override
	protected void runImpl() {
		if (stonespearId >= 1 && stonespearId <= 6) { //idk sometimes it sends different bytes! TODO
			LegionDominionLocation location = LegionDominionService.getInstance().getLegionDominionLoc(stonespearId);
			Legion legion = getConnection().getActivePlayer().getLegion();
			sendPacket(new SM_LEGION_DOMINION_RANK(location, legion));
		}
	}

}
