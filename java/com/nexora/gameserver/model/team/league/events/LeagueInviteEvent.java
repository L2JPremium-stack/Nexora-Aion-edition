package com.nexora.gameserver.model.team.league.events;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.nexora.gameserver.model.team.league.League;
import com.nexora.gameserver.model.team.league.LeagueService;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class LeagueInviteEvent extends RequestResponseHandler<Player> {

	private final Player invited;

	public LeagueInviteEvent(Player requester, Player invited) {
		super(requester);
		this.invited = invited;
	}

	@Override
	public void acceptRequest(Player requester, Player responder) {
		if (LeagueService.canInvite(requester, invited)) {
			League league = requester.getPlayerAlliance().getLeague();

			if (league == null) {
				league = LeagueService.createLeague(requester);
			}
			if (!invited.isInLeague()) {
				LeagueService.addAlliance(league, invited.getPlayerAlliance());
			}
		}
	}

	@Override
	public void denyRequest(Player requester, Player responder) {
		PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_REJECT_INVITATION(responder.getName()));
	}

}
