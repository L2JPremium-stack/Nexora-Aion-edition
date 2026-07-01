package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatupEffect")
public class StatupEffect extends BufEffect {

	@Override
	public void endEffect(Effect effect) {
		super.endEffect(effect);
		effect.getEffected().getLifeStats().updateCurrentStats();
	}

}
