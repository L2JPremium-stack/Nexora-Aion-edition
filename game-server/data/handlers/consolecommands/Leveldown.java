package consolecommands;

import com.nexora.gameserver.configs.main.GSConfig;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.ConsoleCommand;

/**
 * @author ginho1, Neon
 */
public class Leveldown extends ConsoleCommand {

	public Leveldown() {
		super("leveldown", "Levels a player down.");

		setSyntaxInfo("<value> - Levels your target down by the specified number of levels.");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length < 1) {
			sendInfo(admin);
			return;
		}

		final VisibleObject target = admin.getTarget();
		if (!(target instanceof Player)) {
			PacketSendUtility.sendPacket(admin, SM_SYSTEM_MESSAGE.STR_INVALID_TARGET());
			return;
		}

		final Player player = (Player) target;
		int newLevel;
		try {
			newLevel = player.getLevel() - Integer.parseInt(params[0]);
		} catch (NumberFormatException e) {
			sendInfo(admin, "Please specify the number of levels to subtract.");
			return;
		}

		if (newLevel < 1 || newLevel > GSConfig.PLAYER_MAX_LEVEL) {
			sendInfo(admin, "Invalid level.");
			return;
		}

		player.getCommonData().setLevel(newLevel);
		sendInfo(admin, "Set " + player.getName() + "'s level to " + player.getLevel());
	}
}
