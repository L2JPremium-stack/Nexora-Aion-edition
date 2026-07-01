package com.nexora.gameserver.controllers;

import java.util.concurrent.ConcurrentHashMap;

import com.nexora.gameserver.controllers.observer.RoadObserver;
import com.nexora.gameserver.model.animations.ObjectDeleteAnimation;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.road.Road;

/**
 * @author SheppeR
 */
public class RoadController extends VisibleObjectController<Road> {

	ConcurrentHashMap<Integer, RoadObserver> observed = new ConcurrentHashMap<>();

	@Override
	public void see(VisibleObject object) {
		if (object instanceof Player) {
			Player p = (Player) object;
			RoadObserver observer = new RoadObserver(getOwner(), p);
			p.getObserveController().addObserver(observer);
			observed.put(p.getObjectId(), observer);
		}
	}

	@Override
	public void notSee(VisibleObject object, ObjectDeleteAnimation animation) {
		if (object instanceof Player) {
			Player p = (Player) object;
			RoadObserver observer = observed.remove(p.getObjectId());
			p.getObserveController().removeObserver(observer);
		}
	}
}
