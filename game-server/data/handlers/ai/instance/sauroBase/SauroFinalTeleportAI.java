package ai.instance.sauroBase;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Cheatkiller
 */
@AIName("saurohiddenpassage")
public class SauroFinalTeleportAI extends NpcAI {

	public SauroFinalTeleportAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		switchWay(player, dialogActionId);
		return true;
	}

	private void switchWay(Player player, int dialogActionId) {
		switch (dialogActionId) {
			case SELECT_BOSS_LEVEL3:
				checkKeys(player, 1);
				break;
			case SELECT_BOSS_LEVEL4:
				checkKeys(player, 2);
				break;
		}
	}

	private void checkKeys(Player player, long keyCount) {
		Item keys = player.getInventory().getFirstItemByItemId(185000179);
		int portal = (int) (730875 + keyCount);
		if (keys != null && keys.getItemCount() >= keyCount) {
			spawn(portal, 127.5f, 432.8f, 151, (byte) 119);
			player.getInventory().decreaseByItemId(185000179, keys.getItemCount());
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
			AIActions.deleteOwner(this);
			PacketSendUtility.broadcastToMap(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDVritra_Base_DoorOpen_09());
		} else {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1352));
		}
	}
}
