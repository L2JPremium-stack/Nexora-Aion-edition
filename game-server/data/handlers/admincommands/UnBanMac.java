package admincommands;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.BannedMacManager;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author KID
 */
public class UnBanMac extends AdminCommand {

	public UnBanMac() {
		super("unbanmac");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params == null || params.length < 1) {
			info(player, null);
			return;
		}

		String address = params[0];
		boolean result = BannedMacManager.getInstance().unbanAddress(address,
			"uban;mac=" + address + ", " + player.getObjectId() + "; admin=" + player.getName());
		if (result)
			PacketSendUtility.sendMessage(player, "mac " + address + " has unbanned");
		else
			PacketSendUtility.sendMessage(player, "mac " + address + " is not banned");
	}

	@Override
	public void info(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: //unbanmac <mac>");
	}
}
