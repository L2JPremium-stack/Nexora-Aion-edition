package com.nexora.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;

import com.nexora.gameserver.model.SkillElement;

/**
 * @author ATracer
 */
@XmlEnum
public enum ItemAttackType {
	PHYSICAL(false, SkillElement.NONE),
	MAGICAL_EARTH(true, SkillElement.EARTH),
	MAGICAL_WATER(true, SkillElement.WATER),
	MAGICAL_WIND(true, SkillElement.WIND),
	MAGICAL_FIRE(true, SkillElement.FIRE);

	private boolean magic;
	private SkillElement elem;

	private ItemAttackType(boolean magic, SkillElement elem) {
		this.magic = magic;
		this.elem = elem;
	}

	public boolean isMagical() {
		return magic;
	}

	public SkillElement getMagicalElement() {
		return elem;
	}
}
