package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author VladimirZ, Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvulnerableWingEffect")
public class InvulnerableWingEffect extends EffectTemplate {

	@Override
	public void calculate(Effect effect) {
		// Only for players
		if (effect.getEffected() instanceof Player)
			super.calculate(effect, null, null);
	}

	@Override
	public void applyEffect(final Effect effect) {
		effect.addToEffectedController();
		effect.getEffected().getEffectController().setAbnormal(AbnormalState.INVULNERABLE_WING);
	}

	@Override
	public void endEffect(Effect effect) {
		effect.getEffected().getEffectController().unsetAbnormal(AbnormalState.INVULNERABLE_WING);
	}
}
