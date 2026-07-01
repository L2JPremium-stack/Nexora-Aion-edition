package ai.instance.crucibleChallenge;

import static com.nexora.gameserver.model.DialogAction.SETPRO1;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.zone.ZoneName;

/**
 * @author xTz
 */
@AIName("administratorarminos")
public class AdministratorArminosAI extends NpcAI {

	public AdministratorArminosAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		if (dialogActionId == SETPRO1) {
			if (player.isInsideZone(ZoneName.get("ILLUSION_STADIUM_1_300320000"))) {
				spawn(217827, 1250.1598f, 237.97736f, 405.3968f, (byte) 0);
				spawn(217828, 1250.1598f, 239.97736f, 405.3968f, (byte) 0);
				spawn(217829, 1250.1598f, 235.97736f, 405.3968f, (byte) 0);
			} else if (player.isInsideZone(ZoneName.get("ILLUSION_STADIUM_6_300320000"))) {
				spawn(217827, 1265.9661f, 793.5348f, 436.64008f, (byte) 0);
				spawn(217828, 1265.9661f, 789.5348f, 436.6402f, (byte) 0);
				spawn(217829, 1265.9661f, 791.5348f, 436.64014f, (byte) 0);
			}
			AIActions.deleteOwner(this);
		}
		return true;
	}
}
