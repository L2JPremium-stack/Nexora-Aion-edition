package com.nexora.gameserver.model.siege;

import com.nexora.gameserver.model.templates.siegelocation.SiegeLocationTemplate;

/**
 * @author Estrayl
 */
public class AgentLocation extends SiegeLocation {

	public AgentLocation(SiegeLocationTemplate template) {
		super(template);
	}

	@Override
	public int getNextState() {
		return isVulnerable() ? STATE_INVULNERABLE : STATE_VULNERABLE;
	}

	@Override
	public SiegeRace getRace() {
		return SiegeRace.BALAUR;
	}
}
