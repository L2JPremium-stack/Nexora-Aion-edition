package com.nexora.gameserver.questEngine.task.checker;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.world.zone.ZoneName;

/**
 * @author ATracer, Neon
 */
public class ZoneChecker extends DestinationChecker {

	protected final ZoneName zoneName;

	public ZoneChecker(Creature follower, ZoneName zoneName) {
		super(follower);
		this.zoneName = zoneName;
	}

	@Override
	public boolean check() {
		return follower.isInsideZone(zoneName);
	}
}
