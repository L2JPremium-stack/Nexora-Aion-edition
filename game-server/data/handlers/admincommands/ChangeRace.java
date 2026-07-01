package admincommands;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author ginho1
 */
public class ChangeRace extends AdminCommand {

	public ChangeRace() {
		super("changerace", "Switches your race to the opposite faction.");
	}

	@Override
	public void execute(Player admin, String... params) {
		admin.getCommonData().setRace(admin.getOppositeRace());
		admin.getController().onChangedPlayerAttributes();
		admin.getController().updateNearbyQuests();
	}
}
