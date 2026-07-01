package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.model.house.HouseRegistry;
import com.nexora.gameserver.model.templates.housing.HousingEmblem;

/**
 * @author Rolandas
 */
public class EmblemObject extends HouseObject<HousingEmblem> {

	public EmblemObject(HouseRegistry registry, int objId, int templateId) {
		super(registry, objId, templateId);
	}

	@Override
	public boolean canExpireNow() {
		return false;
	}

}
