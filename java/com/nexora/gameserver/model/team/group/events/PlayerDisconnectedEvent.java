package com.nexora.gameserver.model.team.group.events;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.TeamEvent;
import com.nexora.gameserver.model.team.common.legacy.GroupEvent;
import com.nexora.gameserver.model.team.group.PlayerGroup;
import com.nexora.gameserver.model.team.group.PlayerGroupService;
import com.nexora.gameserver.network.aion.serverpackets.SM_GROUP_MEMBER_INFO;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class PlayerDisconnectedEvent implements TeamEvent {

	private final PlayerGroup group;
	private final Player player;

	public PlayerDisconnectedEvent(PlayerGroup group, Player player) {
		this.group = group;
		this.player = player;
	}

	/**
	 * Player should be in group before disconnection
	 */
	@Override
	public boolean checkCondition() {
		return group.hasMember(player.getObjectId());
	}

	@Override
	public void handleEvent() {
		if (group.getOnlineMembers().isEmpty()) {
			PlayerGroupService.disband(group);
		} else {
			if (player.equals(group.getLeader().getObject())) {
				group.onEvent(new ChangeGroupLeaderEvent(group));
			}
			group.forEach(member -> {
				if (!member.equals(player)) {
					PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_PARTY_HE_BECOME_OFFLINE(player.getName()));
					PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(group, player, GroupEvent.DISCONNECTED));
					// disconnect other group members on logout? check
					PacketSendUtility.sendPacket(player, new SM_GROUP_MEMBER_INFO(group, member, GroupEvent.DISCONNECTED));
				}
			});
		}
	}

}
