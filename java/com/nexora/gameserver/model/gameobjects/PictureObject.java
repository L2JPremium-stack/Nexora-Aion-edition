package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.house.HouseRegistry;
import com.nexora.gameserver.model.templates.housing.HousingPicture;

/**
 * @author Rolandas
 */
public class PictureObject extends HouseObject<HousingPicture> {

	public PictureObject(HouseRegistry registry, int objId, int templateId) {
		super(registry, objId, templateId);
	}

	@Override
	public void onUse(Player player) {

	}

}
