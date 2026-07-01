package com.nexora.gameserver.questEngine.task.checker;

import com.nexora.gameserver.model.gameobjects.Creature;

/**
 * @author ATracer, Neon
 */
public abstract class DestinationChecker {

	protected final Creature follower;

	DestinationChecker(Creature follower) {
		this.follower = follower;
	}

	public Creature getFollower() {
		return follower;
	}

	public abstract boolean check();
}
