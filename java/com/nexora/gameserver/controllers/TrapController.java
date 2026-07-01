package com.nexora.gameserver.controllers;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.services.summons.TrapService;

public class TrapController extends NpcController {

	@Override
	public void onDie(Creature lastAttacker) {
		TrapService.unregisterTrap(getOwner().getObjectId());
		super.onDie(lastAttacker);
	}

	@Override
	public void onDelete() {
		TrapService.unregisterTrap(getOwner().getObjectId());
		super.onDelete();
	}

}
