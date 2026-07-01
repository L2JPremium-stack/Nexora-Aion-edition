package consolecommands;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.ConsoleCommand;

/**
 * @author ginho1
 */
public class Partyrecall extends ConsoleCommand {

	public Partyrecall() {
		super("partyrecall");
	}

	@Override
	public void execute(Player admin, String... params) {
		PacketSendUtility.sendMessage(admin, "Command not implemented.");
		return;
	}
}
