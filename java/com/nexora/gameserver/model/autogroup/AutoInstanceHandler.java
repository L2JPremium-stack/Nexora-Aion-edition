package com.nexora.gameserver.model.autogroup;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.world.WorldMapInstance;

/**
 * @author xTz
 */
public interface AutoInstanceHandler {

	void onInstanceCreate(WorldMapInstance instance);

	AGQuestion addLookingForParty(LookingForParty lookingForParty);

	void onEnterInstance(Player player);

	void onLeaveInstance(Player player);

	void onPressEnter(Player player);

	void unregister(Player player);
}
