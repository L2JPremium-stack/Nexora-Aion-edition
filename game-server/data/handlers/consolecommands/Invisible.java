package consolecommands;

import java.util.Collections;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureVisualState;
import com.nexora.gameserver.network.aion.serverpackets.SM_ABNORMAL_STATE;
import com.nexora.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.skillengine.effect.AbnormalState;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.ConsoleCommand;

/**
 * @author ginho1, Neon
 */
public class Invisible extends ConsoleCommand {

	public Invisible() {
		super("invisible", "Sets advanced invisibility.");
	}

	@Override
	public void execute(Player player, String... params) {
		if (!player.isInVisualState(CreatureVisualState.HIDE20)) {
			player.getEffectController().setAbnormal(AbnormalState.HIDE);
			player.setVisualState(CreatureVisualState.HIDE20);
			player.getController().onHide();
			PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
		}
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_EFFECT_INVISIBLE_BEGIN());
		// required because without a skill this isn't sent automatically (outdated abnormals can cause issues when opening a private store for example)
		PacketSendUtility.sendPacket(player, new SM_ABNORMAL_STATE(Collections.emptyList(), player.getEffectController().getAbnormals(), 0));
	}
}
