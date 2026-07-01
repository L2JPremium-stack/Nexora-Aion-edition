package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.controllers.observer.ActionObserver;
import com.nexora.gameserver.controllers.observer.AttackShieldObserver;
import com.nexora.gameserver.controllers.observer.DeathObserver;
import com.nexora.gameserver.controllers.observer.ObserverType;
import com.nexora.gameserver.model.gameobjects.Summon;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.ShieldType;

/**
 * @author Sippolo, kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProtectEffect")
public class ProtectEffect extends ShieldEffect {

	@Override
	public void startEffect(final Effect effect) {
		AttackShieldObserver asObserver = new AttackShieldObserver(value, hitvalue, percent, false, effect, hitType, getType(), hitTypeProb, 0, radius,
			null, 0);
		effect.addObserver(effect.getEffected(), asObserver);

		if (effect.getEffector() instanceof Summon) {
			effect.addObserver(effect.getEffector(), new ActionObserver(ObserverType.SUMMONRELEASE) {

				@Override
				public void summonrelease() {
					effect.endEffect();
				}

			});
		} else {
			effect.addObserver(effect.getEffector(), new DeathObserver(_ -> effect.endEffect()));
		}
	}

	@Override
	public void endEffect(Effect effect) {
	}

	@Override
	public ShieldType getType() {
		return ShieldType.PROTECT;
	}
}
