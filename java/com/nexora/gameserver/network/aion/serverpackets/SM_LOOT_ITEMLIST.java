package com.nexora.gameserver.network.aion.serverpackets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.drop.Drop;
import com.nexora.gameserver.model.drop.DropItem;
import com.nexora.gameserver.model.gameobjects.DropNpc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.item.ItemTemplate;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author alexa026, Avol, Metos, ATracer, KID, Sykra
 */
public class SM_LOOT_ITEMLIST extends AionServerPacket {

	private final int targetObjectId;
	private final boolean teamMembersNearby;
	private final List<DropItem> dropItems;

	public SM_LOOT_ITEMLIST(DropNpc dropNpc, Set<DropItem> setItems, Player player) {
		this.targetObjectId = dropNpc.getObjectId();
		Collection<Player> playersInRange = dropNpc.getInRangePlayers();
		this.teamMembersNearby = playersInRange.size() > 1 && playersInRange.contains(player);
		this.dropItems = new ArrayList<>();
		for (DropItem item : setItems)
			if (item.canViewDropItem(player.getObjectId()))
				dropItems.add(item);
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player activePlayer = con.getActivePlayer();
		if (activePlayer == null)
			return;
		writeD(targetObjectId);
		writeC(dropItems.size());

		for (DropItem dropItem : dropItems) {
			Drop drop = dropItem.getDropTemplate();
			writeC(dropItem.getIndex()); // index in droplist
			writeD(drop.getItemId());
			writeD((int) dropItem.getCount());
			writeC(dropItem.getOptionalSocket());
			writeC(0);
			writeC(0); // 3.5
			ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(drop.getItemId());
			boolean showLootConfirmation = !template.isTradeable();
			if (dropItem.isOnlyPossibleLooter(activePlayer) || !teamMembersNearby)
				showLootConfirmation = false;
			writeC(showLootConfirmation ? 1 : 0);
		}

	}

}
