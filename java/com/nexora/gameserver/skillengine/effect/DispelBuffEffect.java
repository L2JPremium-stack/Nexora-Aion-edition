package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.skillengine.model.DispelCategoryType;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.SkillTargetSlot;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DispelBuffEffect")
public class DispelBuffEffect extends AbstractDispelEffect {

	@Override
	public void applyEffect(Effect effect) {
		super.applyEffect(effect, DispelCategoryType.BUFF, SkillTargetSlot.BUFF);
	}
}
