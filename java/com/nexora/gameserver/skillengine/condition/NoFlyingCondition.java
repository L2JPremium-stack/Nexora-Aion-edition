package com.nexora.gameserver.skillengine.condition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.Skill;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NoFlyingCondition")
public class NoFlyingCondition extends Condition {

	@Override
	public boolean validate(Skill env) {
		return (!env.getEffector().isFlying());
	}

	@Override
	public boolean validate(Effect effect) {
		return (!effect.getEffected().isFlying());
	}

}
