package com.nexora.gameserver.model.stats.calc.functions;

import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.utils.stats.CalculationType;

/**
 * @author ATracer
 */
public class StatSubFunction extends StatFunction {

	@Override
	public void apply(Stat2 stat, CalculationType... calculationTypes) {
		if (isBonus()) {
			stat.addToBonus(-getValue());
		} else {
			stat.addToBase(-getValue());
		}
	}

	@Override
	public final int getPriority() {
		return isBonus() ? 60 : 30;
	}

}
