package com.nexora.gameserver.taskmanager.tasks;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.alliance.PlayerAllianceService;
import com.nexora.gameserver.model.team.common.legacy.GroupEvent;
import com.nexora.gameserver.model.team.common.legacy.PlayerAllianceEvent;
import com.nexora.gameserver.model.team.group.PlayerGroupService;
import com.nexora.gameserver.taskmanager.AbstractFIFOPeriodicTaskManager;

/**
 * Supports PlayerGroup and PlayerAlliance movement updating.
 * 
 * @author Sarynth
 */
public final class TeamMoveUpdater extends AbstractFIFOPeriodicTaskManager<Player> {

	private static final class SingletonHolder {

		private static final TeamMoveUpdater INSTANCE = new TeamMoveUpdater();
	}

	public static TeamMoveUpdater getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public TeamMoveUpdater() {
		super(2000);
	}

	@Override
	protected void callTask(Player player) {
		if (player.isOnline()) {
			if (player.isInGroup()) {
				PlayerGroupService.updateGroup(player, GroupEvent.MOVEMENT);
			} else if (player.isInAlliance()) {
				PlayerAllianceService.updateAlliance(player, PlayerAllianceEvent.MOVEMENT);
			}
		}
	}

	@Override
	protected String getCalledMethodName() {
		return "teamMoveUpdate()";
	}

}
