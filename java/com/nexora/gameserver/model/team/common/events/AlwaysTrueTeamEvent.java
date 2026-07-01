package com.nexora.gameserver.model.team.common.events;

import com.nexora.gameserver.model.team.TeamEvent;

/**
 * @author ATracer
 */
public abstract class AlwaysTrueTeamEvent implements TeamEvent {

	@Override
	public final boolean checkCondition() {
		return true;
	}

}
