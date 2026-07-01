package com.nexora.gameserver.model.broker.filter;

import com.nexora.gameserver.model.templates.item.ItemTemplate;

/**
 * @author ATracer
 */
public abstract class BrokerFilter {

	/**
	 * @param template
	 * @return
	 */
	public abstract boolean accept(ItemTemplate template);
}
