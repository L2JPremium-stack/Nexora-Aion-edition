package com.nexora.gameserver.model.stats.container;

import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.services.LifeStatsRestoreService;

/**
 * @author ATracer
 */
public class NpcLifeStats extends CreatureLifeStats<Npc> {

	public NpcLifeStats(Npc owner) {
		super(owner, owner.getGameStats().getMaxHp().getCurrent(), owner.getGameStats().getMaxMp().getCurrent());
	}

	@Override
	public void triggerRestoreTask() {
		synchronized (restoreLock) {
			if (lifeRestoreTask == null && !isDead()) {
				this.lifeRestoreTask = LifeStatsRestoreService.getInstance().scheduleHpRestoreTask(this);
			}
		}
	}
}
