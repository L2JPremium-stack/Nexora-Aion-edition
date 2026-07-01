package com.nexora.gameserver.world.zone;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.zone.ZoneInfo;
import com.nexora.gameserver.model.templates.zone.ZoneType;

public class NoFlyZoneInstance extends ZoneInstance {

	public NoFlyZoneInstance(int mapId, ZoneInfo template) {
		super(mapId, template);
	}

	@Override
	public synchronized boolean onEnter(Creature creature) {
		if (!super.onEnter(creature))
			return false;
		boolean wasInNoFlyZone = creature.isInsideZoneType(ZoneType.NO_FLY);
		creature.setInsideZoneType(ZoneType.NO_FLY);
		if (!wasInNoFlyZone && creature.isInsideZoneType(ZoneType.FLY) && creature instanceof Player player)
			player.getController().onLeaveFlyArea();
		return true;
	}

	@Override
	public synchronized boolean onLeave(Creature creature) {
		if (!super.onLeave(creature))
			return false;
		creature.unsetInsideZoneType(ZoneType.NO_FLY);
		if (!creature.isInsideZoneType(ZoneType.NO_FLY) && creature.isInsideZoneType(ZoneType.FLY) && creature instanceof Player player)
			player.getController().onEnterFlyArea();
		return true;
	}
}
