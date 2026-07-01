package admincommands;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureSeeState;
import com.nexora.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.nexora.gameserver.utils.ChatUtil;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Mathew
 */
public class See extends AdminCommand {

	public See() {
		super("see", "Lets you see hidden npcs and players.");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (admin.getSeeState() < 2) {
			admin.setSeeState(CreatureSeeState.SEARCH20);
			sendInfo(admin, ChatUtil.l10n(288645)); // Can see targets in advanced hide states.
		} else {
			admin.unsetSeeState(CreatureSeeState.SEARCH20);
			sendInfo(admin, "You lost vision.");
		}
		PacketSendUtility.broadcastPacket(admin, new SM_PLAYER_STATE(admin), true);
		admin.updateKnownlist();
	}
}
