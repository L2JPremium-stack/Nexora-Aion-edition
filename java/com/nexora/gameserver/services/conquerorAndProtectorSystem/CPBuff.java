package com.nexora.gameserver.services.conquerorAndProtectorSystem;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.stats.calc.StatOwner;
import com.nexora.gameserver.model.templates.cp.CPRank;
import com.nexora.gameserver.model.templates.cp.CPType;

/**
 * @author Dtem
 */
public class CPBuff implements StatOwner {

	public void applyEffect(Player player, CPType type, int rank) {
		endEffect(player);

		if (rank == 0)
			return;

		CPRank cpRank = DataManager.CONQUEROR_AND_PROTECTOR_DATA.getRank(type, rank);
		if (cpRank != null && !cpRank.getStatModifiers().isEmpty())
			player.getGameStats().addEffect(this, cpRank.getStatModifiers());
	}

	public void endEffect(Player player) {
		player.getGameStats().endEffect(this);
	}
}
