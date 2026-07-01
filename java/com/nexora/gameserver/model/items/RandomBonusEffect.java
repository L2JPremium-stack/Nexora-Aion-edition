package com.nexora.gameserver.model.items;

import java.util.List;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.stats.calc.StatOwner;
import com.nexora.gameserver.model.stats.calc.functions.StatFunction;
import com.nexora.gameserver.model.templates.item.bonuses.StatBonusType;

/**
 * @author xTz
 */
public class RandomBonusEffect implements StatOwner {

	private final int statBonusId;
	private final List<StatFunction> stats;

	public RandomBonusEffect(StatBonusType type, int statBonusSetId, int statBonusId) {
		this.statBonusId = statBonusId;
		this.stats = DataManager.ITEM_RANDOM_BONUSES.getTemplate(type, statBonusSetId, statBonusId).getModifiers();
	}

	public int getStatBonusId() {
		return statBonusId;
	}

	public void applyEffect(Player player) {
		player.getGameStats().addEffect(this, stats);
	}

	public void endEffect(Player player) {
		player.getGameStats().endEffect(this);
	}
}
