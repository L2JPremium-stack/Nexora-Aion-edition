package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.controllers.observer.ActionObserver;
import com.nexora.gameserver.controllers.observer.ObserverType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChangeHateOnAttackedEffect")
public class ChangeHateOnAttackedEffect extends EffectTemplate {

	@XmlAttribute
	protected int value1;// delta
	@XmlAttribute
	protected int value2;

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(final Effect effect) {
		// TODO: maybe this isn't correct formula?
		final int finalValue = value1 + value2;

		effect.addObserver(effect.getEffected(), new ActionObserver(ObserverType.ATTACKED) {

			@Override
			public void attacked(Creature creature, int skillId) {
				if (creature instanceof Npc)
					creature.getAggroList().addHate(effect.getEffected(), finalValue);
			}
		});
	}
}
