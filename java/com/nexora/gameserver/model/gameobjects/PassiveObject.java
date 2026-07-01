package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.model.house.HouseRegistry;
import com.nexora.gameserver.model.templates.housing.HousingPassiveItem;

/**
 * @author Rolandas
 */
public class PassiveObject extends HouseObject<HousingPassiveItem> {

	public PassiveObject(HouseRegistry registry, int objId, int templateId) {
		super(registry, objId, templateId);
	}

}
