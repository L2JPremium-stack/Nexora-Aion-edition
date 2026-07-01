package com.nexora.gameserver.model.team.group.events;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.common.events.PlayerEnteredEvent;
import com.nexora.gameserver.model.team.common.legacy.GroupEvent;
import com.nexora.gameserver.model.team.group.PlayerGroup;
import com.nexora.gameserver.model.team.group.PlayerGroupService;
import com.nexora.gameserver.network.aion.serverpackets.SM_ABYSS_RANK_UPDATE;
import com.nexora.gameserver.network.aion.serverpackets.SM_GROUP_INFO;
import com.nexora.gameserver.network.aion.serverpackets.SM_GROUP_MEMBER_INFO;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class PlayerGroupEnteredEvent extends PlayerEnteredEvent<PlayerGroup> {

	public PlayerGroupEnteredEvent(PlayerGroup group, Player player) {
		super(group, player);
	}

	@Override
	public void handleEvent() {
		PlayerGroupService.addPlayerToGroup(team, player);
		PacketSendUtility.sendPacket(player, new SM_GROUP_INFO(team));
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_ENTERED_PARTY());
		PacketSendUtility.sendPacket(player, new SM_GROUP_MEMBER_INFO(team, player, GroupEvent.JOIN));
		team.sendBrands(player);
		team.forEach(member -> {
			if (!member.equals(player)) {
				// TODO probably here JOIN event
				PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(team, player, GroupEvent.ENTER));
				PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_PARTY_HE_ENTERED_PARTY(player.getName()));
				PacketSendUtility.sendPacket(player, new SM_GROUP_MEMBER_INFO(team, member, GroupEvent.ENTER));
			}
		});
		PacketSendUtility.broadcastPacket(player, new SM_ABYSS_RANK_UPDATE(1, player), true);
		super.handleEvent();
	}

}
