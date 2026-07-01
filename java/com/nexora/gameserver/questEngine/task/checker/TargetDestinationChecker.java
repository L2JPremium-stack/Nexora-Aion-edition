package com.nexora.gameserver.questEngine.task.checker;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.utils.PositionUtil;

/**
 * @author ATracer, Neon
 */
public class TargetDestinationChecker extends DestinationChecker {

	protected final Creature target;

	public TargetDestinationChecker(Creature follower, Creature target) {
		super(follower);
		this.target = target;
	}

	@Override
	public boolean check() {
		return PositionUtil.isInRange(target, follower, 20);
	}
}
