package com.nexora.gameserver.controllers.attack;

import com.nexora.gameserver.model.gameobjects.Creature;

/**
 * @author ATracer
 */
public class PlayerAggroList extends AggroList {

	public PlayerAggroList(Creature owner) {
		super(owner);
	}

	@Override
	protected boolean isAware(Creature creature) {
		return creature != null && owner.getKnownList().knows(creature);
	}
}
