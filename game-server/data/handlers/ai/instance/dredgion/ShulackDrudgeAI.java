package ai.instance.dredgion;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.CreatureType;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.services.item.ItemService;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI;

/**
 * @author cheatkiller
 */
@AIName("shulackdrudge")
public class ShulackDrudgeAI extends GeneralNpcAI {

	public ShulackDrudgeAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogFinish(Player player) {
		addItems(player);
		super.handleDialogFinish(player);
	}

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	private void addItems(Player player) {
		int itemId = player.getRace() == Race.ELYOS ? 182212606 : 182212607;
		Item dredgionSupplies = player.getInventory().getFirstItemByItemId(itemId);
		if (dredgionSupplies == null) {
			ItemService.addItem(player, itemId, 1);
			getOwner().overrideNpcType(CreatureType.PEACE);
		}
	}
}
