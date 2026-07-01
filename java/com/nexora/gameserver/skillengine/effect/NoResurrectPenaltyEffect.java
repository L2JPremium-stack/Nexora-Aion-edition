package com.nexora.gameserver.skillengine.effect;

import com.nexora.gameserver.skillengine.model.Effect;

public class NoResurrectPenaltyEffect extends BufEffect {

	@Override
	public void calculate(Effect effect) {
		effect.addSuccessEffect(this);
	}
}
