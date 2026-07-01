package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.house.HouseRegistry;
import com.nexora.gameserver.model.templates.housing.HousingMoveableItem;

/**
 * @author Rolandas
 */
public class MoveableObject extends HouseObject<HousingMoveableItem> {

	public MoveableObject(HouseRegistry registry, int objId, int templateId) {
		super(registry, objId, templateId);
	}

	@Override
	public void onUse(Player player) {

	}

}
