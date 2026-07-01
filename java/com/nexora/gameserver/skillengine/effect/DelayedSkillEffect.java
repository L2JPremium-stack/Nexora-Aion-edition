package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAttribute;

import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author kecimis, Cheatkiller
 */
public class DelayedSkillEffect extends EffectTemplate {

	@XmlAttribute(name = "skill_id")
	protected int skillId;

	@Override
	public void applyEffect(final Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void endEffect(Effect effect) {
		super.endEffect(effect);
		if (effect.isEndedByTime())
			SkillEngine.getInstance().applyEffectsDirectly(skillId, effect.getEffector(), effect.getEffected(), effect.getTargetX(), effect.getTargetY(), effect.getTargetZ());
	}
}
