package com.nexora.gameserver.world.knownlist;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.VisibleObject;

/**
 * @author ATracer
 */
public class CreatureAwareKnownList extends KnownList {

	public CreatureAwareKnownList(VisibleObject owner) {
		super(owner);
	}

	@Override
	protected final boolean isAwareOf(VisibleObject newObject) {
		return super.isAwareOf(newObject) && newObject instanceof Creature;
	}
}
