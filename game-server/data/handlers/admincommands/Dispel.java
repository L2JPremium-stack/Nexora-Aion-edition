package admincommands;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Hilgert, Estrayl
 */
public class Dispel extends AdminCommand {

	public Dispel() {
		super("dispel", "Removes all effects including transformations.");
	}

	@Override
	public void execute(Player admin, String... params) {
		VisibleObject target = admin.getTarget();
		if (target == null)
			target = admin;

		if (target instanceof Creature creature) {
			creature.getEffectController().removeAllEffects();
			creature.getEffectController().removeTransformEffects();
			sendInfo(admin, "Removed all effects of " + target + ".");
		}
	}
}
