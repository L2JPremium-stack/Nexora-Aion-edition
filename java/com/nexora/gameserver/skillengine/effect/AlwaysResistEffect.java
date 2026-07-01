package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.controllers.attack.AttackStatus;
import com.nexora.gameserver.controllers.observer.AttackStatusObserver;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AlwaysResistEffect")
public class AlwaysResistEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(Effect effect) {
		effect.addObserver(effect.getEffected(), new AttackStatusObserver(value, AttackStatus.RESIST) {

			@Override
			public boolean checkStatus(AttackStatus status) {
				if (status == AttackStatus.RESIST) {
					if (--value <= 0)
						effect.endEffect();
					return true;
				}
				return false;
			}

		});
	}
}
