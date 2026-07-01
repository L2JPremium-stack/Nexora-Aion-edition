package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.HealType;

/**
 * @author ATracer, vlog, Sippolo, kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealInstantEffect")
public class HealInstantEffect extends AbstractHealEffect {

	@Override
	public void applyEffect(Effect effect) {
		super.applyEffect(effect, HealType.HP);
	}

	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, HealType.HP);
	}

	@Override
	public int getCurrentStatValue(Effect effect) {
		return effect.getEffected().getLifeStats().getCurrentHp();
	}

	@Override
	public int getMaxStatValue(Effect effect) {
		return effect.getEffected().getGameStats().getMaxHp().getCurrent();
	}
}
