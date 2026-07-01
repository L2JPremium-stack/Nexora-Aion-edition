package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.controllers.attack.AttackStatus;
import com.nexora.gameserver.controllers.observer.AttackerCriticalStatus;
import com.nexora.gameserver.controllers.observer.AttackerCriticalStatusObserver;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OneTimeBoostSkillCriticalEffect")
public class OneTimeBoostSkillCriticalEffect extends EffectTemplate {

	@XmlAttribute
	private int count;
	@XmlAttribute
	private boolean percent;

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(final Effect effect) {
		effect.addObserver(effect.getEffected(), new AttackerCriticalStatusObserver(AttackStatus.CRITICAL, count, value, percent) {

			@Override
			public AttackerCriticalStatus checkAttackerCriticalStatus(AttackStatus stat, boolean isSkill) {
				if (stat == status && isSkill) {
					if (getCount() <= 1)
						effect.endEffect();
					else
						decreaseCount();

					acStatus.setResult(true);
				} else
					acStatus.setResult(false);

				return acStatus;
			}
		});
	}
}
