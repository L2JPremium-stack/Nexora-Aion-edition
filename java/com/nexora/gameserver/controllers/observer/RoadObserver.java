package com.nexora.gameserver.controllers.observer;

import com.nexora.gameserver.geoEngine.math.Vector3f;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.road.Road;
import com.nexora.gameserver.model.templates.road.RoadExit;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.world.WorldType;

/**
 * @author SheppeR
 */
public class RoadObserver extends ActionObserver {

	private final Player player;
	private final Road road;
	private Vector3f oldPosition;

	public RoadObserver(Road road, Player player) {
		super(ObserverType.MOVE);
		this.player = player;
		this.road = road;
		this.oldPosition = new Vector3f(player.getX(), player.getY(), player.getZ());
	}

	@Override
	public void moved() {
		Vector3f newPosition = new Vector3f(player.getX(), player.getY(), player.getZ());
		if (road.isCrossed(oldPosition, newPosition)) {
			RoadExit exit = road.getTemplate().getRoadExit();

			WorldType type = road.getWorldType();
			if (type == WorldType.ELYSEA) {
				if (player.getRace() == Race.ELYOS) {
					TeleportService.teleportTo(player, exit.getMap(), exit.getX(), exit.getY(), exit.getZ(), (byte) 0, TeleportAnimation.FADE_OUT_BEAM);
				}
			} else if (type == WorldType.ASMODAE) {
				if (player.getRace() == Race.ASMODIANS) {
					TeleportService.teleportTo(player, exit.getMap(), exit.getX(), exit.getY(), exit.getZ(), (byte) 0, TeleportAnimation.FADE_OUT_BEAM);
				}
			} else {
				TeleportService.teleportTo(player, exit.getMap(), exit.getX(), exit.getY(), exit.getZ(), (byte) 0, TeleportAnimation.FADE_OUT_BEAM);
			}
		}
		oldPosition = newPosition;
	}
}
