package admincommands;

import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;
import com.nexora.gameserver.world.World;

/**
 * @author VladimirZ, Neon
 */
public class Online extends AdminCommand {

	public Online() {
		super("online", "Shows the number of online players.");
	}

	@Override
	public void execute(Player admin, String... params) {
		int elyosCount = 0;
		int asmoCount = 0;

		for (Player player : World.getInstance().getAllPlayers()) {
			if (player.getRace() == Race.ELYOS)
				elyosCount++;
			else
				asmoCount++;
		}

		String countInfo = (elyosCount + asmoCount) + " (" + elyosCount + " Elyos / " + asmoCount + " Asmo" + (asmoCount == 1 ? ")" : "s)");
		PacketSendUtility.sendPacket(admin, SM_SYSTEM_MESSAGE.STR_LIST_USER(countInfo));
	}
}
