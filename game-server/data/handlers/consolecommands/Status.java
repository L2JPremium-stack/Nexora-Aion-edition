package consolecommands;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_GM_SHOW_PLAYER_STATUS;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.ConsoleCommand;
import com.nexora.gameserver.world.World;

/**
 * @author Yeats
 */
public class Status extends ConsoleCommand {

	public Status() {
		super("status");
	}

	@Override
	protected void execute(Player admin, String... params) {
		Player target = null;
		if (params.length > 0) {
			target = World.getInstance().getPlayer(params[0]);
		}
		if (target == null && admin.getTarget() instanceof Player) {
			target = (Player) admin.getTarget();
		}
		if (target != null) {
			PacketSendUtility.sendPacket(admin, new SM_GM_SHOW_PLAYER_STATUS(target));
		}
	}
}
