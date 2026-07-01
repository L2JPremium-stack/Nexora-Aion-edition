package com.nexora.gameserver.controllers.attack;

import com.nexora.gameserver.model.gameobjects.AionObject;

public class DamageInfo<T extends AionObject> {

	private final T attacker;
	private int damage;

	public DamageInfo(T attacker) {
		this.attacker = attacker;
	}

	public T getAttacker() {
		return attacker;
	}

	public int getDamage() {
		return damage;
	}

	void addDamage(int damage) {
		this.damage += damage;
	}
}
