package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.skillengine.model.DispelCategoryType;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.SkillTargetSlot;

/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DispelNpcBuffEffect")
public class DispelNpcBuffEffect extends AbstractDispelEffect {

	@Override
	public void applyEffect(Effect effect) {
		super.applyEffect(effect, DispelCategoryType.NPC_BUFF, SkillTargetSlot.BUFF);
	}
}
