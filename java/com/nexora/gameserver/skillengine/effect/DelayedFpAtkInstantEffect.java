package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAttribute;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.nexora.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author kecimis
 */
public class DelayedFpAtkInstantEffect extends EffectTemplate {

	@XmlAttribute
	protected int delay;
	@XmlAttribute
	protected boolean percent;

	@Override
	public void calculate(Effect effect) {
		// Only players have FP
		if (effect.getEffected() instanceof Player)
			super.calculate(effect, null, null);
	}

	@Override
	public void applyEffect(final Effect effect) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				calculateAndApplyDamage(effect);
			}
		}, delay);
	}

	private void calculateAndApplyDamage(Effect effect) {
		if (!effect.getEffector().isEnemy(effect.getEffected()))
			return;
		int valueWithDelta = calculateBaseValue(effect);
		Player player = (Player) effect.getEffected();
		int maxFP = player.getLifeStats().getMaxFp();

		int newValue = valueWithDelta;
		// Support for values in percentage
		if (percent)
			newValue = (maxFP * valueWithDelta) / 100;

		player.getLifeStats().reduceFp(TYPE.FP_DAMAGE, newValue, effect.getSkillId(), LOG.FPATTACK);
	}

}
