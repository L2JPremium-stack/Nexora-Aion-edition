package consolecommands;

import com.nexora.gameserver.model.PlayerClass;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.ClassChangeService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.ConsoleCommand;

/**
 * @author ginho1, Neon
 */
public class Changeclass extends ConsoleCommand {

	public Changeclass() {
		super("changeclass", "Changes a players class.");

		setSyntaxInfo("<class> - Changes your characters class to the one specified.");
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

		Player player = (Player) target;
		String newClass = params[0];

		if (newClass.equalsIgnoreCase("fighter"))
			newClass = "GLADIATOR";
		else if (newClass.equalsIgnoreCase("knight"))
			newClass = "TEMPLAR";
		else if (newClass.equalsIgnoreCase("wizard"))
			newClass = "SORCERER";
		else if (newClass.equalsIgnoreCase("elementalist"))
			newClass = "SPIRIT_MASTER";

		PlayerClass playerClass;
		try {
			playerClass = PlayerClass.valueOf(newClass.toUpperCase());
		} catch (IllegalArgumentException e) {
			sendInfo(admin, "Invalid player class.");
			return;
		}

		ClassChangeService.setClass(player, playerClass, false, true);
		sendInfo(admin, "You have changed " + player.getName() + "'s class to " + playerClass.toString().toLowerCase() +  ".");
	}
}
