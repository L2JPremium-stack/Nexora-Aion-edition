package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.controllers.attack.AttackUtil;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.nexora.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.EffectReserved;
import com.nexora.gameserver.skillengine.model.EffectReserved.ResourceType;

/**
 * @author ATracer, kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BleedEffect")
public class BleedEffect extends AbstractOverTimeEffect {

	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, StatEnum.BLEED_RESISTANCE, null);
	}

	@Override
	public void startEffect(final Effect effect) {
		int valueWithDelta = calculateBaseValue(effect);
		int finalDamage = AttackUtil.calculateMagicalOverTimeSkillResult(effect, valueWithDelta, this, false);
		effect.setReserveds(new EffectReserved(position, finalDamage, ResourceType.HP, true, false), true);
		super.startEffect(effect, AbnormalState.BLEED);
	}

	@Override
	public void endEffect(Effect effect) {
		super.endEffect(effect, AbnormalState.BLEED);
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		Creature effected = effect.getEffected();
		effected.getController().onAttack(effect, TYPE.DAMAGE, effect.getReserveds(position).getValue(), false, LOG.BLEED, hopType);
		effected.getObserveController().notifyDotAttackedObservers(effect.getEffector(), effect);
	}
}
