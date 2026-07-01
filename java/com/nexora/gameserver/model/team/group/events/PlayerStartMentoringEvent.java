package com.nexora.gameserver.model.team.group.events;

import java.util.function.Consumer;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.common.events.AlwaysTrueTeamEvent;
import com.nexora.gameserver.model.team.common.legacy.GroupEvent;
import com.nexora.gameserver.model.team.group.PlayerGroup;
import com.nexora.gameserver.network.aion.serverpackets.SM_ABYSS_RANK_UPDATE;
import com.nexora.gameserver.network.aion.serverpackets.SM_GROUP_MEMBER_INFO;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.audit.AuditLogger;
import com.nexora.gameserver.utils.collections.Predicates;

/**
 * @author ATracer
 */
public class PlayerStartMentoringEvent extends AlwaysTrueTeamEvent implements Consumer<Player> {

	private final PlayerGroup group;
	private final Player player;

	public PlayerStartMentoringEvent(PlayerGroup group, Player player) {
		this.group = group;
		this.player = player;
	}

	@Override
	public void handleEvent() {
		if (group.filterMembers(Predicates.Players.canBeMentoredBy(player)).isEmpty()) {
			AuditLogger.log(player, "sent fake start mentoring packet");
			return;
		}
		player.setMentor(true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_MENTOR_START());
		group.forEach(this);
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ABYSS_RANK_UPDATE(2, player));
	}

	@Override
	public void accept(Player member) {
		if (!player.equals(member)) {
			PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_MENTOR_START_PARTYMSG(player.getName()));
		}
		PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(group, player, GroupEvent.MOVEMENT));
	}
}
