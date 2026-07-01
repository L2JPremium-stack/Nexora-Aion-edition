package com.nexora.gameserver.model.templates.materials;

import java.util.function.Predicate;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.Summon;
import com.nexora.gameserver.model.gameobjects.player.Player;

/**
 * @author Rolandas
 */
@XmlType(name = "MaterialTarget")
@XmlEnum
public enum MaterialTarget {

	ALL(c -> true),
	NPC(c -> c instanceof Npc),
	PLAYER(c -> c instanceof Player),
	PLAYER_WITH_PET(c -> PLAYER.matches(c) || c instanceof Summon && ((Summon) c).getMaster() != null);

	private final Predicate<Creature> isTargetCheck;

	MaterialTarget(Predicate<Creature> isTargetPredicate) {
		this.isTargetCheck = isTargetPredicate;
	}

	public boolean matches(Creature creature) {
		return isTargetCheck.test(creature);
	}

}
