package com.nexora.gameserver.model.team.common.events;

import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.TeamEvent;
import com.nexora.gameserver.model.team.TeamMember;
import com.nexora.gameserver.model.team.TemporaryPlayerTeam;
import com.nexora.gameserver.network.aion.serverpackets.SM_LEAVE_GROUP_MEMBER;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.event.EventService;
import com.nexora.gameserver.services.instance.InstanceService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author ATracer
 */
public abstract class PlayerLeavedEvent<TM extends TeamMember<Player>, T extends TemporaryPlayerTeam<TM>> implements TeamEvent {

	public enum LeaveReson {
		BAN,
		LEAVE,
		LEAVE_TIMEOUT,
		DISBAND
	}

	protected final T team;
	protected final Player leavedPlayer;
	protected final LeaveReson reason;
	protected final String banPersonName;

	public PlayerLeavedEvent(T team, Player player) {
		this(team, player, LeaveReson.LEAVE);
	}

	public PlayerLeavedEvent(T team, Player player, LeaveReson reason) {
		this(team, player, reason, "");
	}

	public PlayerLeavedEvent(T team, Player player, LeaveReson reason, String banPersonName) {
		this.team = team;
		this.leavedPlayer = player;
		this.reason = reason;
		this.banPersonName = banPersonName;
	}

	/**
	 * Player should be in team to broadcast this event
	 */
	@Override
	public boolean checkCondition() {
		return team.hasMember(leavedPlayer.getObjectId());
	}

	@Override
	public void handleEvent() {
		if (leavedPlayer.isOnline()) {
			PacketSendUtility.sendPacket(leavedPlayer, new SM_LEAVE_GROUP_MEMBER());
			if (team.equals(leavedPlayer.getPosition().getWorldMapInstance().getRegisteredTeam())) {
				PacketSendUtility.sendPacket(leavedPlayer, SM_SYSTEM_MESSAGE.STR_MSG_LEAVE_INSTANCE_NOT_PARTY());
				leavedPlayer.getController().addTask(TaskId.INSTANCE_KICK, ThreadPoolManager.getInstance().schedule(() -> {
					if (leavedPlayer.getCurrentTeamId() != team.getObjectId()) {
						if (team.equals(leavedPlayer.getPosition().getWorldMapInstance().getRegisteredTeam()))
							InstanceService.moveToExitPoint(leavedPlayer);
					}
				}, 30000));
			}
		}
		EventService.getInstance().onLeftTeam(leavedPlayer, team);
	}
}
