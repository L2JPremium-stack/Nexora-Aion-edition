package com.nexora.gameserver.model.instance.instanceposition;

import com.nexora.gameserver.model.gameobjects.player.Player;

/**
 * @author xTz
 */
public interface InstancePositionHandler {

	void initialize(int mapId, int instanceId);

	void port(Player player, int zone, int position);
}
