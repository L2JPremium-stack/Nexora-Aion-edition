package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DiseaseEffect")
public class DiseaseEffect extends EffectTemplate {

	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, StatEnum.DISEASE_RESISTANCE, null);
	}

	// skillId 18386
	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(Effect effect) {
		Creature effected = effect.getEffected();
		effect.setAbnormal(AbnormalState.DISEASE);
		effected.getEffectController().setAbnormal(AbnormalState.DISEASE);
	}

	@Override
	public void endEffect(Effect effect) {
		if (effect.getEffected().getEffectController().isAbnormalSet(AbnormalState.DISEASE))
			effect.getEffected().getEffectController().unsetAbnormal(AbnormalState.DISEASE);
	}

}
