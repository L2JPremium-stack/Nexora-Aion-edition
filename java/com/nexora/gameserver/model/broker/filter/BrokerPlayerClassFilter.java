package com.nexora.gameserver.model.broker.filter;

import com.nexora.gameserver.model.PlayerClass;
import com.nexora.gameserver.model.templates.item.ItemTemplate;

/**
 * @author ATracer
 */
public class BrokerPlayerClassFilter extends BrokerFilter {

	private PlayerClass playerClass;

	/**
	 * @param playerClass
	 */
	public BrokerPlayerClassFilter(PlayerClass playerClass) {
		super();
		this.playerClass = playerClass;
	}

	@Override
	public boolean accept(ItemTemplate template) {
		return template.isClassSpecific(playerClass);
	}

}
