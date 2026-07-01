package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.templates.npc.NpcTemplate;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author ATracer, Cheatkiller
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolymorphEffect")
public class PolymorphEffect extends TransformEffect {

	@Override
	public void startEffect(Effect effect) {
		super.startEffect(effect);
		if (model > 0) {
			Creature effected = effect.getEffected();
			NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(model);
			if (template != null)
				effected.getTransformModel().setTribe(template.getTribe());
		}
	}

	@Override
	public void endEffect(Effect effect) {
		super.endEffect(effect);
		effect.getEffected().getTransformModel().setTribe(null);
	}
}
