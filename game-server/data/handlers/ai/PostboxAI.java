package ai;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.DialogPage;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.services.player.PlayerMailboxState;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
@AIName("postbox")
public class PostboxAI extends NpcAI {

	public PostboxAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		player.getMailbox().mailBoxState = PlayerMailboxState.REGULAR;
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), DialogPage.MAIL.id()));
	}

	@Override
	protected void handleDialogFinish(Player player) {
	}

}
