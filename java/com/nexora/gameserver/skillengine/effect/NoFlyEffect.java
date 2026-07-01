package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NoFlyEffect")
public class NoFlyEffect extends EffectTemplate {

	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, StatEnum.NOFLY_RESISTANCE, null);
	}

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	protected boolean isDodgedOrResisted(Effect effect, StatEnum statEnum) {
		if (effect.getEffected().getEffectController().isInAnyAbnormalState(AbnormalState.INVULNERABLE_WING)) {
			return true;
		}
		return super.isDodgedOrResisted(effect, statEnum);
	}

	@Override
	public void startEffect(Effect effect) {
		if (effect.getEffected() instanceof Player player) {
			player.getFlyController().endFly(true);
		}
		effect.setAbnormal(AbnormalState.NOFLY);
		effect.getEffected().getEffectController().setAbnormal(AbnormalState.NOFLY);
	}

	@Override
	public void endEffect(Effect effect) {
		effect.getEffected().getEffectController().unsetAbnormal(AbnormalState.NOFLY);
	}
}
