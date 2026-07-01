package com.nexora.gameserver.world.knownlist;

import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.world.WorldMapInstance;

public class FlagKnownList extends PlayerAwareKnownList {

	public FlagKnownList(Npc owner) {
		if (!owner.isFlag())
			throw new IllegalArgumentException();
		super(owner);
	}

	@Override
	public synchronized void update() {
		WorldMapInstance worldMapInstance = owner.getPosition().getWorldMapInstance();
		knownObjects.values().removeIf(knownObject -> knownObject.get().getWorldMapInstance() != worldMapInstance);
		worldMapInstance.forEachPlayer(player -> {
			if (player.getKnownList().add(owner))
				add(player);
		});
	}

	@Override
	protected float getVisibleDistance() {
		return Float.MAX_VALUE;
	}
}
