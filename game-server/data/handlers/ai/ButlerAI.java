package ai;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.DialogPage;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.house.House;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Rolandas, Neon, Sykra
 */
@AIName("butler")
public class ButlerAI extends GeneralNpcAI {

	public ButlerAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		return kickDialog(player, DialogPage.getByActionId(dialogActionId));
	}

	private boolean kickDialog(Player player, DialogPage page) {
		if (page == DialogPage.NULL)
			return false;
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), page.id()));
		return true;
	}

	@Override
	protected void handleCreatureSee(Creature creature) {
		if (creature instanceof Player player && getCreator() instanceof House house)
			house.sendScripts(player);
	}
}
