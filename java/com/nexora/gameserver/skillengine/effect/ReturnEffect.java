package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReturnEffect")
public class ReturnEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		TeleportService.moveToBindLocation((Player) effect.getEffector());
	}

	@Override
	public void calculate(Effect effect) {
		if (effect.getEffected().isSpawned())
			effect.addSuccessEffect(this);
	}
}
