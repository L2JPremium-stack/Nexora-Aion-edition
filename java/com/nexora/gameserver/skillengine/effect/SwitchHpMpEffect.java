package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.stats.container.CreatureLifeStats;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SwitchHpMpEffect")
public class SwitchHpMpEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		CreatureLifeStats<? extends Creature> lifeStats = effect.getEffected().getLifeStats();
		int currentHp = lifeStats.getCurrentHp();
		int currentMp = lifeStats.getCurrentMp();

		// doesn't send sm_attack_status, checked on 4.5
		lifeStats.setCurrentHp(currentMp, effect.getEffector());
		lifeStats.setCurrentMp(currentHp);
	}
}
