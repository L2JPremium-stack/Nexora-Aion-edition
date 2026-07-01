package consolecommands;

import java.util.List;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.ChatUtil;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.ConsoleCommand;
import com.nexora.gameserver.world.World;

/**
 * @author ginho1
 */
public class Clearusercoolt extends ConsoleCommand {

	public Clearusercoolt() {
		super("clearusercoolt", "Clears cooldowns for instances.");
	}

	@Override
	public void execute(Player admin, String... params) {
		String playerName = ChatUtil.getRealCharName(params[0], true);
		Player player = World.getInstance().getPlayer(playerName);
		if (player == null) {
			PacketSendUtility.sendPacket(admin, SM_SYSTEM_MESSAGE.STR_NO_SUCH_USER(playerName));
			return;
		}
		clearAllInstanceCooldowns(admin, player);
	}

	public static void clearAllInstanceCooldowns(Player admin, Player player) {
		if (player.getPortalCooldownList().getPortalCoolDowns() == null) {
			PacketSendUtility.sendMessage(admin, (player.equals(admin) ? "You have" : player.getName() + " has") + " no instance cooldowns to remove.");
			return;
		}

		List<Integer> worldIds = player.getPortalCooldownList().getPortalCoolDowns().keySet().stream().toList();
		player.getPortalCooldownList().setPortalCoolDowns(null);
		worldIds.forEach(player.getPortalCooldownList()::sendEntryInfo);

		if (player.equals(admin)) {
			PacketSendUtility.sendMessage(admin, "Your instance cooldowns were removed.");
		} else {
			PacketSendUtility.sendMessage(admin, "You have removed instance cooldowns of " + player.getName() + '.');
			PacketSendUtility.sendMessage(player, admin.getName(true) + " removed your instance cooldowns.");
		}
	}
}
