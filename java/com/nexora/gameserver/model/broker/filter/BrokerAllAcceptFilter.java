package com.nexora.gameserver.model.broker.filter;

import com.nexora.gameserver.model.templates.item.ItemTemplate;

/**
 * @author ATracer
 */
public class BrokerAllAcceptFilter extends BrokerFilter {

	@Override
	public boolean accept(ItemTemplate template) {
		return true;
	}
}
