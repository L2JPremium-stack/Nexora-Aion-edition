package com.nexora.gameserver.model.stats.calc.functions;

import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.calc.StatOwner;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.utils.stats.CalculationType;

/**
 * @author ATracer, Rolandas
 */
public interface IStatFunction extends Comparable<IStatFunction> {

	StatEnum getName();

	boolean isBonus();

	int getPriority();

	int getValue();

	boolean validate(Stat2 stat);

	void apply(Stat2 stat, CalculationType... calculationTypes);

	StatOwner getOwner();

	boolean hasConditions();

	@Override
	default int compareTo(IStatFunction o) {
		return getPriority() - o.getPriority();
	}
}
