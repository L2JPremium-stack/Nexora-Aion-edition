package com.nexora.gameserver.controllers.observer;

import com.nexora.gameserver.controllers.attack.AttackStatus;

/**
 * @author kecimis
 */
public class AttackerCriticalStatusObserver extends AttackCalcObserver {

	protected AttackerCriticalStatus acStatus = null;
	protected AttackStatus status;

	public AttackerCriticalStatusObserver(AttackStatus status, int count, int value, boolean isPercent) {
		this.status = status;
		this.acStatus = new AttackerCriticalStatus(count, value, isPercent);
	}

	public int getCount() {
		return acStatus.getCount();
	}

	public void decreaseCount() {
		acStatus.setCount((acStatus.getCount() - 1));
	}
}
