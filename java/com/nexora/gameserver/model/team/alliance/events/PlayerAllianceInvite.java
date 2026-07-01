package com.nexora.gameserver.model.team.alliance.events;

import java.util.ArrayList;
import java.util.List;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.nexora.gameserver.model.team.TeamType;
import com.nexora.gameserver.model.team.alliance.PlayerAlliance;
import com.nexora.gameserver.model.team.alliance.PlayerAllianceService;
import com.nexora.gameserver.model.team.group.PlayerGroup;
import com.nexora.gameserver.model.team.group.PlayerGroupService;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.restrictions.PlayerRestrictions;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.collections.Predicates;

/**
 * @author ATracer
 */
public class PlayerAllianceInvite extends RequestResponseHandler<Player> {

	public PlayerAllianceInvite(Player inviter) {
		super(inviter);
	}

	@Override
	public void acceptRequest(Player inviter, Player invited) {
		if (PlayerRestrictions.canInviteToAlliance(inviter, invited)) {

			PlayerAlliance alliance = inviter.getPlayerAlliance();
			List<Player> playersToAdd = new ArrayList<>();
			collectPlayersToAdd(inviter, invited, playersToAdd, alliance);

			if (alliance == null) {
				alliance = PlayerAllianceService.createAlliance(inviter, invited, TeamType.ALLIANCE);
				playersToAdd.remove(invited);
			}

			for (Player member : playersToAdd) {
				PlayerAllianceService.addPlayer(alliance, member);
			}
		}
	}

	private void collectPlayersToAdd(Player inviter, Player invited, List<Player> playersToAdd, PlayerAlliance alliance) {
		// Collect requester Group without leader
		if (inviter.isInGroup()) {
			if (alliance != null)
				throw new IllegalArgumentException("If requester is in group, alliance should be null");
			PlayerGroup group = inviter.getPlayerGroup();
			playersToAdd.addAll(group.filterMembers(Predicates.Players.allExcept(inviter)));

			for (Player player : group.getMembers())
				PlayerGroupService.removePlayer(player);
		}

		// Collect full Invited Group
		if (invited.isInGroup()) {
			PlayerGroup group = invited.getPlayerGroup();
			playersToAdd.addAll(group.getMembers());
			for (Player player : group.getMembers())
				PlayerGroupService.removePlayer(player);
		} else { // or just single player
			playersToAdd.add(invited);
		}
	}

	@Override
	public void denyRequest(Player requester, Player responder) {
		PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_REJECT_INVITATION(responder.getName()));
	}

}
