package consolecommands;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_GM_SEARCH;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.ConsoleCommand;
import com.nexora.gameserver.world.World;

/**
 * @author ginho1
 */
public class Search extends ConsoleCommand {

	public Search() {
		super("search");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length > 0) {
			Player p = World.getInstance().getPlayer(params[0]);
			if (p != null)
				PacketSendUtility.sendPacket(admin, new SM_GM_SEARCH(p));
		}
	}
}
