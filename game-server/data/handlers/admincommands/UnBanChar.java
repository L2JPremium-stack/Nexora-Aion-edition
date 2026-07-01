package admincommands;

import com.nexora.gameserver.dao.PlayerDAO;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.services.PunishmentService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.Util;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author nrg
 */
public class UnBanChar extends AdminCommand {

	public UnBanChar() {
		super("unbanchar");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(admin, "Syntax: //unbanchar <player>");
			return;
		}

		// Banned player must be offline
		String name = Util.convertName(params[0]);
		int playerId = PlayerDAO.getPlayerIdByName(name);
		if (playerId == 0) {
			PacketSendUtility.sendMessage(admin, "Player " + name + " was not found!");
			PacketSendUtility.sendMessage(admin, "Syntax: //unbanchar <player>");
			return;
		}

		PacketSendUtility.sendMessage(admin, "Character " + name + " is not longer banned!");

		PunishmentService.unbanChar(playerId);
	}

	@Override
	public void info(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: //unban <player> [account|ip|full]");
	}
}
