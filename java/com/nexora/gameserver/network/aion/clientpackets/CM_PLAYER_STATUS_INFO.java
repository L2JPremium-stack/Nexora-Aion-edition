package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.alliance.PlayerAllianceService;
import com.nexora.gameserver.model.team.common.events.TeamCommand;
import com.nexora.gameserver.model.team.common.service.PlayerTeamCommandService;
import com.nexora.gameserver.model.team.league.LeagueService;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;

/**
 * Called when entering the world and during group management
 *
 * @author Lyahim, ATracer, Simple, xTz
 */
public class CM_PLAYER_STATUS_INFO extends AionClientPacket {

	private int commandCode;
	private int selectedObjectId;
	private int allianceGroupId;
	private int secondObjectId;

	public CM_PLAYER_STATUS_INFO(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		commandCode = readUC();
		selectedObjectId = readD();
		allianceGroupId = readD();
		secondObjectId = readD();
	}

	@Override
	protected void runImpl() {
		Player activePlayer = getConnection().getActivePlayer();
		TeamCommand command = TeamCommand.getCommand(commandCode);
		switch (command) {
			case GROUP_SET_LFG:
				activePlayer.setLookingForGroup(selectedObjectId == 2);
				break;
			case ALLIANCE_CHANGE_GROUP:
				PlayerAllianceService.changeMemberGroup(activePlayer, selectedObjectId, secondObjectId, allianceGroupId);
				break;
			case LEAGUE_ALLIANCE_MOVE:
				LeagueService.moveAlliance(activePlayer, selectedObjectId, allianceGroupId);
				break;
			default:
				PlayerTeamCommandService.executeCommand(activePlayer, command, selectedObjectId);
		}
	}

}
