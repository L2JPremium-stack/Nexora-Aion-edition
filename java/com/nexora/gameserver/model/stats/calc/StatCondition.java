package com.nexora.gameserver.model.stats.calc;

import com.nexora.gameserver.model.stats.calc.functions.IStatFunction;

/**
 * @author ATracer
 */
public interface StatCondition {

	/**
	 * Validate that function should be applied to the stat
	 * 
	 * @param stat
	 * @param statFunction
	 * @return
	 */
	boolean validate(Stat2 stat, IStatFunction statFunction);
}
