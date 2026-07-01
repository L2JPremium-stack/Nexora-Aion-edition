package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.skillengine.change.Change;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoostSkillCastingTimeEffect")
public class BoostSkillCastingTimeEffect extends BufEffect {

	@Override
	public void calculate(Effect effect) {
		if (effect.getEffected().isEnemy(effect.getEffector())) {
			if (change != null) {
				for (Change c : change) {
					if (c.getValue() < 0) {
						super.calculate(effect, StatEnum.SLOW_RESISTANCE, null);
						return;
					}
				}
			}
		}
		super.calculate(effect);
	}

}
