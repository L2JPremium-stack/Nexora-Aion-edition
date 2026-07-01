package com.nexora.gameserver.model.team.common.events;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.TeamMember;
import com.nexora.gameserver.model.team.TemporaryPlayerTeam;
import com.nexora.gameserver.network.aion.serverpackets.SM_ABYSS_RANK_UPDATE;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public abstract class PlayerStopMentoringEvent<T extends TemporaryPlayerTeam<? extends TeamMember<Player>>> extends AlwaysTrueTeamEvent {

	protected final T team;
	protected final Player player;

	public PlayerStopMentoringEvent(T team, Player player) {
		this.team = team;
		this.player = player;
	}

	@Override
	public void handleEvent() {
		player.setMentor(false);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_MENTOR_END());
		team.forEach(member -> {
			if (!player.equals(member))
				PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_MENTOR_END_PARTYMSG(player.getName()));
			sendGroupPacketOnMentorEnd(member);
		});
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ABYSS_RANK_UPDATE(2, player));
	}

	/**
	 * @param member
	 */
	protected abstract void sendGroupPacketOnMentorEnd(Player member);
}
