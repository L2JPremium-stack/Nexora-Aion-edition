package com.nexora.gameserver.model.team.league.events;

import java.util.List;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.common.events.AlwaysTrueTeamEvent;
import com.nexora.gameserver.model.team.league.League;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Source
 */
public class LeagueKinahDistributionEvent extends AlwaysTrueTeamEvent {

	private final long amount;
	private final Player eventPlayer;

	public LeagueKinahDistributionEvent(Player player, long amount) {
		eventPlayer = player;
		this.amount = amount;
	}

	@Override
	public void handleEvent() {
		if (eventPlayer.getInventory().getKinah() < amount) {
			PacketSendUtility.sendPacket(eventPlayer, SM_SYSTEM_MESSAGE.STR_NOT_ENOUGH_MONEY());
			return;
		}

		League league = eventPlayer.getPlayerAlliance().getLeague();
		List<Player> onlineMembers = league.getOnlineMembers();
		if (onlineMembers.size() > 1 && amount >= onlineMembers.size()) {
			long rewardPerPlayer = amount / onlineMembers.size();
			if (eventPlayer.getInventory().tryDecreaseKinah(amount)) {
				for (Player member : onlineMembers) {
					member.getInventory().increaseKinah(rewardPerPlayer);
					if (member.equals(eventPlayer)) {
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_SPLIT_ME_TO_B(amount, onlineMembers.size(), rewardPerPlayer));
					} else {
						PacketSendUtility.sendPacket(member,
							SM_SYSTEM_MESSAGE.STR_MSG_SPLIT_B_TO_ME(eventPlayer.getName(), amount, onlineMembers.size(), rewardPerPlayer));
					}
				}
			}
		}
	}

}
