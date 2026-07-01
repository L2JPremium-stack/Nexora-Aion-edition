package com.nexora.gameserver.model.trade;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.templates.item.ItemTemplate;

/**
 * @author ATracer, Neon
 */
public class TradeItem {

	private final int itemId;
	protected long count;

	public TradeItem(int itemId, long count) {
		this.itemId = itemId;
		this.count = count;
	}

	public ItemTemplate getItemTemplate() {
		return DataManager.ITEM_DATA.getItemTemplate(itemId);
	}

	public int getItemId() {
		return itemId;
	}

	public long getCount() {
		return count;
	}
}
