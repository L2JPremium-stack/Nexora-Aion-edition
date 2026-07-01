package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.model.house.HouseRegistry;
import com.nexora.gameserver.model.templates.housing.HousingJukeBox;

/**
 * @author Rolandas
 */
public class JukeBoxObject extends HouseObject<HousingJukeBox> {

	public JukeBoxObject(HouseRegistry registry, int objId, int templateId) {
		super(registry, objId, templateId);
	}

}
