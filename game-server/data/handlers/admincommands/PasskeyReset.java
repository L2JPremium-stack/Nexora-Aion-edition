package admincommands;

import com.nexora.gameserver.dao.PlayerDAO;
import com.nexora.gameserver.dao.PlayerPasskeyDAO;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.loginserver.LoginServer;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.Util;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author cura
 */
public class PasskeyReset extends AdminCommand {

	public PasskeyReset() {
		super("passkeyreset");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(player, "syntax: //passkeyreset <player> <passkey>");
			return;
		}

		String name = Util.convertName(params[0]);
		int accountId = PlayerDAO.getAccountIdByName(name);
		if (accountId == 0) {
			PacketSendUtility.sendMessage(player, "player " + name + " can't find!");
			PacketSendUtility.sendMessage(player, "syntax: //passkeyreset <player> <passkey>");
			return;
		}

		try {
			Integer.parseInt(params[1]);
		} catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(player, "parameters should be number!");
			return;
		}

		String newPasskey = params[1];
		if (!(newPasskey.length() > 5 && newPasskey.length() < 9)) {
			PacketSendUtility.sendMessage(player, "passkey is 6~8 digits!");
			return;
		}

		PlayerPasskeyDAO.updateForcePlayerPasskey(accountId, newPasskey);
		LoginServer.getInstance().sendBanPacket((byte) 2, accountId, "", -1, player.getObjectId());
	}

	@Override
	public void info(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax: //passkeyreset <player> <passkey>");
	}
}
