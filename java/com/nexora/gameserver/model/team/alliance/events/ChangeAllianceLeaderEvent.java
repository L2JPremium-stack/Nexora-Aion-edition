package com.nexora.gameserver.model.team.alliance.events;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.alliance.PlayerAlliance;
import com.nexora.gameserver.model.team.alliance.PlayerAllianceMember;
import com.nexora.gameserver.model.team.alliance.PlayerAllianceService;
import com.nexora.gameserver.model.team.alliance.events.AssignViceCaptainEvent.AssignType;
import com.nexora.gameserver.model.team.common.events.ChangeLeaderEvent;
import com.nexora.gameserver.network.aion.serverpackets.SM_ALLIANCE_INFO;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.collections.Predicates;

/**
 * @author ATracer
 */
public class ChangeAllianceLeaderEvent extends ChangeLeaderEvent<PlayerAlliance> {

	public ChangeAllianceLeaderEvent(PlayerAlliance team, Player eventPlayer) {
		super(team, eventPlayer);
	}

	public ChangeAllianceLeaderEvent(PlayerAlliance team) {
		super(team, null);
	}

	@Override
	public void handleEvent() {
		if (eventPlayer == null) {
			for (Integer viceCaptainId : team.getViceCaptainIds()) {
				PlayerAllianceMember viceCaptain = team.getMember(viceCaptainId);
				if (viceCaptain.isOnline()) {
					changeLeaderTo(viceCaptain.getObject());
					return;
				}
			}
			changeLeaderToNextAvailablePlayer();
		} else {
			Player oldLeader = team.getLeaderObject();
			changeLeaderTo(eventPlayer);
			PlayerAllianceService.changeViceCaptain(oldLeader, AssignType.DEMOTE_CAPTAIN_TO_VICECAPTAIN);
		}
	}

	@Override
	protected void changeLeaderTo(final Player player) {
		final boolean inLeague = team.isInLeague();
		team.changeLeader(team.getMember(player.getObjectId()));
		team.getViceCaptainIds().remove(player.getObjectId());
		if (inLeague) {
			team.getLeague().broadcast();
		}
		team.forEach(member -> {
			if (!inLeague) {
				PacketSendUtility.sendPacket(member, new SM_ALLIANCE_INFO(team));
			}
			if (!player.equals(member)) {
				// eventPlayer null only when leader leave by own will and wee not must inform him about new leader
				if (eventPlayer != null) {
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_HE_IS_NEW_LEADER(player.getName()));
				}
				if (inLeague && team.getLeague().getCaptain().equals(player)) {
					team.getLeague().forEach(alliance -> {
						alliance.sendPacket(Predicates.Players.allExcept(player), SM_SYSTEM_MESSAGE.STR_UNION_CHANGE_LEADER_TIMEOUT(player.getName()));
					});
				}
			} else {
				PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_FORCE_YOU_BECOME_NEW_LEADER());
				if (inLeague && team.getLeague().getCaptain().equals(player)) {
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_UNION_YOU_BECOME_NEW_LEADER_TIMEOUT());
				}
			}
		});
	}

}
