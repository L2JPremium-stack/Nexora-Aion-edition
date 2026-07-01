package ai.portals;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.custom.instance.CustomInstanceService;
import com.nexora.gameserver.model.ChatType;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI;

/**
 * @author Estrayl
 */
@AIName("custom_instance_teleporter")
public class CustomInstanceTeleporter extends ActionItemNpcAI {

	public CustomInstanceTeleporter(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		if (player.getLevel() < 65) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL());
			return;
		}
		if (!CustomInstanceService.getInstance().canEnter(player.getObjectId())) {
			PacketSendUtility.sendMessage(player, "You have already done this instance for today. You can re-enter it after 9 AM.",
				ChatType.BRIGHT_YELLOW_CENTER);
			return;
		}
		CustomInstanceService.getInstance().onEnter(player);
	}
}
