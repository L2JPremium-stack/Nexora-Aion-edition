package com.nexora.gameserver.skillengine.effect;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.stats.calc.functions.IStatFunction;
import com.nexora.gameserver.model.templates.stats.ModifiersTemplate;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractAbsoluteStatEffect")
public abstract class AbstractAbsoluteStatEffect extends BufEffect {

	@XmlAttribute(name = "statsetid")
	private int statSetId;

	/**
	 * @param effect
	 * @return
	 */
	@Override
	protected List<IStatFunction> getModifiers(Effect effect) {
		List<IStatFunction> modifiers = new ArrayList<>();
		modifiers.addAll(getModifiersSet().getModifiers());

		return modifiers;
	}

	/**
	 * @return the statSetId
	 */
	public ModifiersTemplate getModifiersSet() {
		return DataManager.ABSOLUTE_STATS_DATA.getTemplate(statSetId);
	}

}
