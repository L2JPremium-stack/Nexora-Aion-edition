package com.nexora.gameserver.model.templates.rewards;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.nexora.commons.utils.Rnd;

@XmlAccessorType(XmlAccessType.FIELD)
public class MedicineItem extends IdLevelReward {

	@Override
	public long getCount() {
		return Rnd.get(1, 3);
	}

}
