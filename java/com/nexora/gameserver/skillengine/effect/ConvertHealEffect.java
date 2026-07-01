package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAttribute;

import com.nexora.gameserver.controllers.observer.AttackShieldObserver;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.HealType;
import com.nexora.gameserver.skillengine.model.ShieldType;

/**
 * @author kecimis
 */
public class ConvertHealEffect extends ShieldEffect {

	@XmlAttribute
	protected HealType type;
	@XmlAttribute(name = "hitpercent")
	protected boolean hitPercent;

	@Override
	public void startEffect(final Effect effect) {
		int valueWithDelta = calculateBaseValue(effect);
		int hitValueWithDelta = hitvalue + hitdelta * effect.getSkillLevel();

		AttackShieldObserver asObserver = new AttackShieldObserver(hitValueWithDelta, valueWithDelta, percent, hitPercent, effect, hitType, getType(),
			hitTypeProb, 0, 0, type, 0);

		effect.addObserver(effect.getEffected(), asObserver);
	}

	@Override
	public void endEffect(Effect effect) {
	}

	@Override
	public ShieldType getType() {
		return ShieldType.CONVERT;
	}

}
