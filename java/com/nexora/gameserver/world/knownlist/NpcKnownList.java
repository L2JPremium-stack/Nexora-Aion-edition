package com.nexora.gameserver.world.knownlist;

import com.nexora.gameserver.model.animations.ObjectDeleteAnimation;
import com.nexora.gameserver.model.gameobjects.VisibleObject;

/**
 * @author ATracer
 */
public class NpcKnownList extends CreatureAwareKnownList {

	public NpcKnownList(VisibleObject owner) {
		super(owner);
	}

	@Override
	public void update() {
		if (owner.getPosition().isMapRegionActive())
			super.update();
		else
			clear(ObjectDeleteAnimation.FADE_OUT);
	}
}
