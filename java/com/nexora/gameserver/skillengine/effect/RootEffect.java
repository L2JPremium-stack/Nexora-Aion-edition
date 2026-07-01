package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.controllers.observer.ActionObserver;
import com.nexora.gameserver.controllers.observer.ObserverType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RootEffect")
public class RootEffect extends EffectTemplate {

	@XmlAttribute
	protected int resistchance = 100;

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, StatEnum.ROOT_RESISTANCE, null);
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		effected.getEffectController().setAbnormal(AbnormalState.ROOT);
		effect.setAbnormal(AbnormalState.ROOT);
		// PacketSendUtility.broadcastPacketAndReceive(effected, new SM_POSITION(effected));
		if (effected instanceof Player player) {
			player.getFlyController().onStopGliding();
			player.getMoveController().abortMove();
		}

		effect.addObserver(effected, new ActionObserver(ObserverType.ATTACKED) {

			@Override
			public void attacked(Creature creature, int skillId) {
				if (Rnd.chance() >= resistchance)
					effected.getEffectController().removeEffect(effect.getSkillId());
			}
		});
	}

	@Override
	public void endEffect(Effect effect) {
		effect.getEffected().getEffectController().unsetAbnormal(AbnormalState.ROOT);
	}
}
