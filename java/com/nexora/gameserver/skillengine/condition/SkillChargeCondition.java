package com.nexora.gameserver.skillengine.condition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.skillengine.model.Skill;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillChargeCondition")
public class SkillChargeCondition extends ChargeCondition {

	@Override
	public boolean validate(Skill env) {
		return true;
	}

	public int getValue() {
		return value;
	}
}
