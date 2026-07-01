package ai;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
@AIName("shifter")
public class ShifterAI extends ActionItemNpcAI {

	public ShifterAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		super.handleUseItemFinish(player);
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(getOwner(), EmotionType.EMOTE, 144, 0), true);
	}
}
