package com.nexora.gameserver.model.base;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;

/**
 * @author Estrayl
 */
public class PanesterraBase extends Base<PanesterraBaseLocation> {

	public PanesterraBase(PanesterraBaseLocation loc) {
		super(loc);
	}

	@Override
	protected int getAssaultDelay() {
		return Rnd.get(75, 200) * 6000;
	}

	@Override
	protected int getAssaultDespawnDelay() {
		return 15 * 60000; // Retail delay
	}

	@Override
	protected int getBossSpawnDelay() {
		return 20 * 60000; // Retail delay
	}

	@Override
	protected int getNpcSpawnDelay() {
		return 5 * 60000; // Retail delay
	}

	@Override
	protected BaseOccupier chooseAssaultRace() {
		return BaseOccupier.BALAUR;
	}

	@Override
	public BaseOccupier getOccupier(Creature bossKiller) {
		if (bossKiller instanceof Player player && player.getPanesterraFaction() != null)
			return BaseOccupier.findBy(player.getPanesterraFaction());
		return getLocation().getTemplate().getDefaultOccupier();
	}
}
