package ai.instance.theShugoEmperorsVault;

import static com.nexora.gameserver.model.DialogAction.TELEPORT_SIMPLE;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.ai.handler.TalkEventHandler;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI;

/**
 * @author Yeats
 */
@AIName("idsweep_in_npc")
public class IDSweep_In_Npc extends GeneralNpcAI {

	public IDSweep_In_Npc(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		TalkEventHandler.onSimpleTalk((NpcAI) getOwner().getAi(), player);
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		if (dialogActionId == TELEPORT_SIMPLE) {
			TeleportService.teleportTo(player, 301400000, player.getInstanceId(), 423.715f, 700.375f, 399f, (byte) 44, TeleportAnimation.FADE_OUT_BEAM);
		}
		return true;
	}

}
