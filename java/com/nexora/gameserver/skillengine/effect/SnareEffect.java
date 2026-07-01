package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SnareEffect")
public class SnareEffect extends BufEffect {

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, StatEnum.SNARE_RESISTANCE, null);
	}

	@Override
	public void endEffect(Effect effect) {
		super.endEffect(effect);
		effect.getEffected().getEffectController().unsetAbnormal(AbnormalState.SNARE);
	}

	@Override
	public void startEffect(Effect effect) {
		super.startEffect(effect);
		effect.getEffected().getEffectController().setAbnormal(AbnormalState.SNARE);
		effect.setAbnormal(AbnormalState.SNARE);
	}
}
