package admincommands;

import java.util.NoSuchElementException;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.services.PunishmentService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.Util;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;
import com.nexora.gameserver.world.World;

/**
 * @author lord_rex Command: //rprison <player> This command is removing player from prison.
 */
public class RPrison extends AdminCommand {

	public RPrison() {
		super("rprison");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length == 0 || params.length > 2) {
			PacketSendUtility.sendMessage(admin, "syntax //rprison <player>");
			return;
		}

		try {
			Player playerFromPrison = World.getInstance().getPlayer(Util.convertName(params[0]));

			if (playerFromPrison != null) {
				PunishmentService.setIsInPrison(playerFromPrison, false, 0, "");
				PacketSendUtility.sendMessage(admin, "Player " + playerFromPrison.getName() + " removed from prison.");
			}
		} catch (NoSuchElementException nsee) {
			PacketSendUtility.sendMessage(admin, "Usage: //rprison <player>");
		} catch (Exception e) {
			PacketSendUtility.sendMessage(admin, "Usage: //rprison <player>");
		}
	}

	@Override
	public void info(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //rprison <player>");
	}
}
