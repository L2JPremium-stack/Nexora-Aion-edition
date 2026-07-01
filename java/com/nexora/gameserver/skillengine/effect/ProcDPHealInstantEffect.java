package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.HealType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcDPHealInstantEffect")
public class ProcDPHealInstantEffect extends AbstractHealEffect {

	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, HealType.DP);
	}

	@Override
	public void applyEffect(Effect effect) {
		super.applyEffect(effect, HealType.DP);
	}

	@Override
	public int getCurrentStatValue(Effect effect) {
		return ((Player) effect.getEffected()).getCommonData().getDp();
	}

	@Override
	public int getMaxStatValue(Effect effect) {
		return ((Player) effect.getEffected()).getGameStats().getMaxDp().getCurrent();
	}

}
