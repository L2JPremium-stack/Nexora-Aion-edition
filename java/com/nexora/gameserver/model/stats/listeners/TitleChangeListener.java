package com.nexora.gameserver.model.stats.listeners;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.stats.container.CreatureGameStats;
import com.nexora.gameserver.model.templates.TitleTemplate;

/**
 * @author xavier
 */
public class TitleChangeListener {

	public static void onBonusTitleChange(CreatureGameStats<?> cgs, int titleId, boolean isSet) {
		TitleTemplate tt = DataManager.TITLE_DATA.getTitleTemplate(titleId);
		if (tt == null) {
			return;
		}
		if (!isSet) {
			cgs.endEffect(tt);
		} else {
			cgs.addEffect(tt, tt.getModifiers());
		}
	}
}
