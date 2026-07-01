package com.nexora.gameserver.world.zone.handler;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.world.zone.ZoneInstance;

/**
 * @author MrPoke
 */
public interface ZoneHandler {

	void onEnterZone(Creature player, ZoneInstance zone);

	void onLeaveZone(Creature player, ZoneInstance zone);
}
