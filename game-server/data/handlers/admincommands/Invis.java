package admincommands;

import java.util.Collections;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureVisualState;
import com.nexora.gameserver.network.aion.serverpackets.SM_ABNORMAL_STATE;
import com.nexora.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.skillengine.effect.AbnormalState;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Divinity, Neon
 */
public class Invis extends AdminCommand {

	public Invis() {
		super("invis", "Sets/unsets advanced invisibility.");
	}

	@Override
	public void execute(Player player, String... params) {
		if (!player.isInVisualState(CreatureVisualState.HIDE20)) {
			player.getEffectController().setAbnormal(AbnormalState.HIDE);
			player.setVisualState(CreatureVisualState.HIDE20);
			player.getController().onHide();
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_EFFECT_INVISIBLE_BEGIN());
		} else {
			player.getEffectController().unsetAbnormal(AbnormalState.HIDE);
			player.unsetVisualState(CreatureVisualState.HIDE20);
			player.getController().onHideEnd();
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_EFFECT_INVISIBLE_END());
		}
		PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
		// required because without a skill this isn't sent automatically (outdated abnormals can cause issues when opening a private store for example)
		PacketSendUtility.sendPacket(player, new SM_ABNORMAL_STATE(Collections.emptyList(), player.getEffectController().getAbnormals(), 0));
	}
}
