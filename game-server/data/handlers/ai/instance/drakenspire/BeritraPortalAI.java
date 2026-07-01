package ai.instance.drakenspire;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.ActionItemNpcAI;

/**
 * @author Estrayl
 */
@AIName("beritra_portal")
public class BeritraPortalAI extends ActionItemNpcAI {

	public BeritraPortalAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(final Player player) {
		switch (Rnd.get(1, 3)) {
			case 1:
				TeleportService.teleportTo(player, 301390000, 174.7f, 518.2f, 1749.6f, (byte) 59, TeleportAnimation.FADE_OUT);
				break;
			case 2:
				TeleportService.teleportTo(player, 301390000, 173.4f, 517.9f, 1749.6f, (byte) 59, TeleportAnimation.FADE_OUT);
				break;
			case 3:
				TeleportService.teleportTo(player, 301390000, 173.4f, 514.6f, 1749.6f, (byte) 59, TeleportAnimation.FADE_OUT);
				break;
		}
		player.getController().startProtectionActiveTask();
		ThreadPoolManager.getInstance().schedule(() -> PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(false, 0, 0, 915, true)), 1000);
	}
}
