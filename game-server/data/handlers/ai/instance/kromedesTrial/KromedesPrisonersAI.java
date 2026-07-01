package ai.instance.kromedesTrial;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Gigi
 */
@AIName("krprisoners")
public class KromedesPrisonersAI extends NpcAI {

	public KromedesPrisonersAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		if (dialogActionId == SETPRO1) {
			AIActions.deleteOwner(this);
		} else if (dialogActionId == SELECT1_1)
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1012));
		return true;
	}

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}
}
