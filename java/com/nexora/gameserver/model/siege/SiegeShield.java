package com.nexora.gameserver.model.siege;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nexora.gameserver.controllers.observer.ActionObserver;
import com.nexora.gameserver.controllers.observer.CollisionDieActor;
import com.nexora.gameserver.geoEngine.scene.DespawnableNode;
import com.nexora.gameserver.geoEngine.scene.Spatial;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.services.SiegeService;
import com.nexora.gameserver.world.zone.ZoneInstance;
import com.nexora.gameserver.world.zone.handler.ZoneHandler;

/**
 * Shields have material ID 11 in geo.
 * 
 * @author Rolandas
 */
public class SiegeShield implements ZoneHandler {

	private final Map<Integer, ActionObserver> observed = new ConcurrentHashMap<>();
	private final Spatial geometry;
	private int siegeLocationId;

	public SiegeShield(Spatial geometry) {
		this.geometry = geometry;
		if (geometry.getParent() instanceof DespawnableNode despawnableNode) {
			despawnableNode.setType(DespawnableNode.DespawnableType.SHIELD);
		}
	}

	public Spatial getGeometry() {
		return geometry;
	}

	@Override
	public void onEnterZone(Creature creature, ZoneInstance zone) {
		if (creature instanceof Player player) {
			FortressLocation loc = SiegeService.getInstance().getFortress(siegeLocationId);
			if (loc.getRace() != SiegeRace.getByRace(player.getRace())) {
				CollisionDieActor shieldObserver = new CollisionDieActor(creature, geometry, loc);
				creature.getObserveController().addObserver(shieldObserver);
				observed.put(creature.getObjectId(), shieldObserver);
			}
		}
	}

	@Override
	public void onLeaveZone(Creature creature, ZoneInstance zone) {
		ActionObserver actionObserver = observed.remove(creature.getObjectId());
		if (actionObserver != null)
			creature.getObserveController().removeObserver(actionObserver);
	}

	public void setSiegeLocationId(int siegeLocationId) {
		this.siegeLocationId = siegeLocationId;
		if (geometry.getParent() instanceof DespawnableNode despawnableNode) {
			despawnableNode.setId(siegeLocationId);
		}
	}

	@Override
	public String toString() {
		return "LocId=" + siegeLocationId + "; Name=" + geometry.getName() + "; Bounds=" + geometry.getWorldBound();
	}

}
