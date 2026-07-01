package ai.instance.aturamSkyFortress;

import static com.nexora.gameserver.model.DialogAction.SETPRO1;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.services.item.ItemService;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author cheatkiller
 */
@AIName("hariken_supply_chest")
public class HarikenSupplyChestAI extends NpcAI {

	public HarikenSupplyChestAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		if (dialogActionId == SETPRO1) {
			addItems(player);
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}

	private void addItems(Player player) {
		Item BottomlessBucket = player.getInventory().getFirstItemByItemId(164000202);
		Item TalonSummoningDevice = player.getInventory().getFirstItemByItemId(164000163);
		if (TalonSummoningDevice == null && BottomlessBucket == null) {
			ItemService.addItem(player, 164000163, 1);
			ItemService.addItem(player, 164000202, 1);
		}
	}
}
