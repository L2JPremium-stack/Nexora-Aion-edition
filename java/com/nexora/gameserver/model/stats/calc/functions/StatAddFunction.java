package com.nexora.gameserver.model.stats.calc.functions;

import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.utils.stats.CalculationType;

/**
 * @author ATracer
 */
public class StatAddFunction extends StatFunction {

	public StatAddFunction() {
	}

	public StatAddFunction(StatEnum name, int value, boolean bonus) {
		super(name, value, bonus);
	}

	@Override
	public void apply(Stat2 stat, CalculationType... calculationTypes) {
		if (isBonus()) {
			stat.addToBonus(getValue());
		} else {
			stat.addToBase(getValue());
		}
	}

	@Override
	public int getPriority() {
		return isBonus() ? 60 : 30;
	}

	@Override
	public String toString() {
		return "StatAddFunction [" + super.toString() + "]";
	}

}
