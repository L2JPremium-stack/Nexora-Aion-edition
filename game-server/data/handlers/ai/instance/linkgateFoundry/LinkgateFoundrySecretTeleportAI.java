package ai.instance.linkgateFoundry;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI;

/**
 * @author Сheatkiller
 */
@AIName("linkgateFoundrySecretRoomTeleport")
public class LinkgateFoundrySecretTeleportAI extends ActionItemNpcAI {

	public LinkgateFoundrySecretTeleportAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		switchFloor(player, dialogActionId);
		return true;
	}

	private void switchFloor(Player player, int dialogActionId) {
		switch (dialogActionId) {
			case SETPRO1:
				TeleportService.teleportTo(player, 301270000, 177.65f, 258.15f, 313, (byte) 60, TeleportAnimation.FADE_OUT_BEAM);
				break;
			case SETPRO2:
				TeleportService.teleportTo(player, 301270000, 176.11f, 258.44f, 354, (byte) 60, TeleportAnimation.FADE_OUT_BEAM);
				break;
			case SETPRO3:
				TeleportService.teleportTo(player, 301270000, 176.11f, 258.44f, 394, (byte) 60, TeleportAnimation.FADE_OUT_BEAM);
				break;
		}
	}
}
