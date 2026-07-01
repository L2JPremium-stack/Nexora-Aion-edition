package consolecommands;

import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.services.CubeExpandService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.ConsoleCommand;

/**
 * @author ginho1
 */
public class Setinventorygrowth extends ConsoleCommand {

	public Setinventorygrowth() {
		super("setinventorygrowth");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length < 1) {
			info(admin, null);
			return;
		}

		final VisibleObject target = admin.getTarget();
		if (target == null) {
			PacketSendUtility.sendMessage(admin, "No target selected.");
			return;
		}

		if (!(target instanceof Player)) {
			PacketSendUtility.sendMessage(admin, "This command can only be used on a player!");
			return;
		}

		final Player player = (Player) target;

		if (CubeExpandService.canExpand(player)) {
			CubeExpandService.npcExpand(player);
			PacketSendUtility.sendMessage(admin, "9 cube slots successfully added to player " + player.getName() + "!");
			PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " gave you a cube expansion!");
		} else {
			PacketSendUtility
				.sendMessage(admin, "Cube expansion cannot be added to " + player.getName() + "!\nReason: player cube already fully expanded.");
			return;
		}
	}

	@Override
	public void info(Player admin, String message) {
		PacketSendUtility.sendMessage(admin, "syntax ///setinventorygrowth <?>");
	}
}
