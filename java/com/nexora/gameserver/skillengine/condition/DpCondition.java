package com.nexora.gameserver.skillengine.condition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.skillengine.model.Skill;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DpCondition")
public class DpCondition extends Condition {

	@XmlAttribute(required = true)
	protected int value;

	@Override
	public boolean validate(Skill skill) {
		return ((Player) skill.getEffector()).getCommonData().getDp() >= value;
	}
}
