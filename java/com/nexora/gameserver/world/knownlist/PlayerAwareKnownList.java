package com.nexora.gameserver.world.knownlist;

import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;

/**
 * @author ATracer
 */
public class PlayerAwareKnownList extends KnownList {

	public PlayerAwareKnownList(VisibleObject owner) {
		super(owner);
	}

	@Override
	protected final boolean isAwareOf(VisibleObject newObject) {
		return super.isAwareOf(newObject) && newObject instanceof Player;
	}

}
