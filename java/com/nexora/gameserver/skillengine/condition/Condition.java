package com.nexora.gameserver.skillengine.condition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.calc.StatCondition;
import com.nexora.gameserver.model.stats.calc.functions.IStatFunction;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.Skill;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Condition")
public abstract class Condition implements StatCondition {

	/**
	 * Validate condition specified in template
	 * 
	 * @param env
	 * @return true or false
	 */
	public abstract boolean validate(Skill env);

	@Override
	public boolean validate(Stat2 stat, IStatFunction statFunction) {
		return true;
	}

	public boolean validate(Effect effect) {
		return true;
	}

}
