package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.HealType;

/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DPHealEffect")
public class DPHealEffect extends HealOverTimeEffect {

	@Override
	public void startEffect(Effect effect) {
		super.startEffect(effect, HealType.DP);
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		super.onPeriodicAction(effect, HealType.DP);
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
