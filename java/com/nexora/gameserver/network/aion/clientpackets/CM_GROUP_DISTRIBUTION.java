package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.alliance.PlayerAllianceService;
import com.nexora.gameserver.model.team.group.PlayerGroupService;
import com.nexora.gameserver.model.team.league.LeagueService;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.restrictions.PlayerRestrictions;

/**
 * @author Lyahim, Simple, xTz
 */
public class CM_GROUP_DISTRIBUTION extends AionClientPacket {

	private long amount;
	private byte partyType;

	public CM_GROUP_DISTRIBUTION(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		amount = readQ();
		partyType = readC();
	}

	@Override
	protected void runImpl() {
		if (amount < 2)
			return;

		Player player = getConnection().getActivePlayer();

		if (!PlayerRestrictions.canTrade(player))
			return;

		switch (partyType) {
			case 1:
				if (player.isInAlliance()) {
					PlayerAllianceService.distributeKinahInGroup(player, amount);
				} else {
					PlayerGroupService.distributeKinah(player, amount);
				}
				break;
			case 2:
				PlayerAllianceService.distributeKinah(player, amount);
				break;
			case 3:
				LeagueService.distributeKinah(player, amount);
				break;
		}
	}

}
