package com.nexora.gameserver.controllers.observer;

import java.util.List;

import com.nexora.gameserver.controllers.attack.AttackResult;
import com.nexora.gameserver.controllers.attack.AttackStatus;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
public class AttackCalcObserver {

	/**
	 * @param status
	 * @return false
	 */
	public boolean checkStatus(AttackStatus status) {
		return false;
	}

	/**
	 * @param attackList
	 * @param effect
	 * @return value
	 */
	public void checkShield(List<AttackResult> attackList, Effect effect, Creature attacker) {

	}

	/**
	 * @param status
	 * @return
	 */
	public boolean checkAttackerStatus(AttackStatus status) {
		return false;
	}

	/**
	 * @param status
	 * @param isSkill
	 * @return
	 */
	public AttackerCriticalStatus checkAttackerCriticalStatus(AttackStatus status, boolean isSkill) {
		return new AttackerCriticalStatus(false);
	}

	/**
	 * @param isSkill
	 * @return physical damage multiplier
	 */
	public float getBasePhysicalDamageMultiplier(boolean isSkill) {
		return 1f;
	}

	/**
	 * @return magic damage multiplier
	 */
	public float getBaseMagicalDamageMultiplier() {
		return 1f;
	}
}
