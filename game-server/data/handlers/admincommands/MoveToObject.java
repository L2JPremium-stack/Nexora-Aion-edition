package admincommands;

import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;
import com.nexora.gameserver.world.World;

/**
 * @author Rolandas
 */
public class MoveToObject extends AdminCommand {

	public MoveToObject() {
		super("movetoobj");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length != 1) {
			PacketSendUtility.sendMessage(admin, "Syntax : //movetoobj <object id>");
			return;
		}

		int objectId = 0;

		try {
			objectId = Integer.valueOf(params[0]);
		} catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(admin, "Only numbers please!!!");
		}

		VisibleObject object = World.getInstance().findVisibleObject(objectId);
		if (object == null) {
			PacketSendUtility.sendMessage(admin, "Cannot find object for spawn #" + objectId);
			return;
		}

		VisibleObject spawn = object;

		TeleportService.teleportTo(admin, spawn.getWorldId(), spawn.getSpawn().getX(), spawn.getSpawn().getY(), spawn.getSpawn().getZ());
	}

	@Override
	public void info(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax : //movetoobj <object id>");
	}

}
