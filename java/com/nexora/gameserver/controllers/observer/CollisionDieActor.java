package com.nexora.gameserver.controllers.observer;

import com.nexora.gameserver.configs.main.GeoDataConfig;
import com.nexora.gameserver.geoEngine.collision.CollisionIntention;
import com.nexora.gameserver.geoEngine.collision.CollisionResult;
import com.nexora.gameserver.geoEngine.collision.CollisionResults;
import com.nexora.gameserver.geoEngine.scene.Spatial;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.siege.FortressLocation;
import com.nexora.gameserver.model.siege.SiegeRace;
import com.nexora.gameserver.services.player.PlayerReviveService;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Rolandas
 */
public class CollisionDieActor extends AbstractCollisionObserver {

	private final FortressLocation fortressLocation;

	public CollisionDieActor(Creature creature, Spatial geometry, FortressLocation fortressLocation) {
		super(creature, geometry, CollisionIntention.MATERIAL.getId(), CheckType.PASS);
		this.fortressLocation = fortressLocation;
	}

	@Override
	public void onMoved(CollisionResults collisionResults) {
		if (collisionResults.size() != 0) {
			if (GeoDataConfig.GEO_MATERIALS_SHOWDETAILS && creature instanceof Player player && player.isStaff()) {
				CollisionResult result = collisionResults.getClosestCollision();
				PacketSendUtility.sendMessage(player, "Entered " + result.getGeometry().getName());
			}
			if (fortressLocation.isUnderShield() && fortressLocation.getRace() != SiegeRace.getByRace(creature.getRace()))
				kill(creature);
		}
	}

	public static void kill(Creature creature) {
		if (creature.getController().die() && creature instanceof Player player)
			PlayerReviveService.scheduleReviveAtBase(player, 2500, 0);
	}
}
