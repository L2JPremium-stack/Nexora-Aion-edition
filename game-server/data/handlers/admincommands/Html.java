package admincommands;

import com.nexora.gameserver.cache.HTMLCache;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.services.HTMLService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author lord_rex
 */
public class Html extends AdminCommand {

	public Html() {
		super("html");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params == null || params.length < 1) {
			PacketSendUtility.sendMessage(player, "Usage: //html <reload|show>");
			return;
		}

		if (params[0].equals("reload")) {
			HTMLCache.getInstance().reload(true);
			PacketSendUtility.sendMessage(player, HTMLCache.getInstance().toString());
		} else if (params[0].equals("show"))
			if (params.length >= 2)
				HTMLService.showHTML(player, HTMLCache.getInstance().getHTML(params[1] + ".xhtml"));
			else
				PacketSendUtility.sendMessage(player, "Usage: //html show <filename>");
	}

	@Override
	public void info(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Usage: //html <reload|show>");
	}
}
