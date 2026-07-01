package com.nexora.gameserver.model.autogroup;

import com.nexora.gameserver.model.PlayerClass;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.player.Player;

/**
 * @author xTz
 */
public record AGPlayer(int objectId, Race race, PlayerClass playerClass, String name) {

	public AGPlayer(Player player) {
		this(player.getObjectId(), player.getRace(), player.getPlayerClass(), player.getName());
	}
}
