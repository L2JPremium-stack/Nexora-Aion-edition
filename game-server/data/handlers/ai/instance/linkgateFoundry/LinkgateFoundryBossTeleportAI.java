package ai.instance.linkgateFoundry;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.DialogPage;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI;

/**
 * @author Cheatkiller
 */
@AIName("linkgateFoundryBossTeleport")
public class LinkgateFoundryBossTeleportAI extends ActionItemNpcAI {

	public LinkgateFoundryBossTeleportAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		selectBoss(player, dialogActionId);
		return true;
	}

	private void selectBoss(Player player, int dialogActionId) {
		int keyId = 185000196;
		int minKeyCount, bossId, msgId;
		boolean spawnGuardian;
		switch (dialogActionId) {
			case SELECT_BOSS_LEVEL2:
				minKeyCount = 1;
				bossId = 234990;
				msgId = 1402440;
				spawnGuardian = true;
				break;
			case SELECT_BOSS_LEVEL3:
				minKeyCount = 5;
				bossId = 233898;
				msgId = 1402441;
				spawnGuardian = true;
				break;
			case SELECT_BOSS_LEVEL4:
				minKeyCount = 7;
				bossId = 234991;
				msgId = 1402442;
				spawnGuardian = false;
				break;
			default:
				return;
		}
		long keyCount = player.getInventory().getItemCountByItemId(keyId);
		if (keyCount < minKeyCount) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), DialogPage.NO_RIGHT.id()));
			return;
		}
		player.getInventory().decreaseByItemId(keyId, keyCount);
		// replace portal
		AIActions.deleteOwner(this);
		spawn(702592, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0, 51);
		// spawn boss and guardian
		PacketSendUtility.broadcastToMap(getOwner(), msgId);
		getOwner().getPosition().getWorldMapInstance().getNpc(233898).getController().delete(); // default belsagos spawn (visible from outside)
		spawn(bossId, 252.2439f, 259.3866f, 312.3536f, (byte) 41);
		if (spawnGuardian)
			spawn(player.getRace() == Race.ELYOS ? 855087 : 855088, 226.76f, 256.7708f, 312.577f, (byte) 0);
		// teleport player
		TeleportService.teleportTo(player, getOwner().getWorldId(), 211.32f, 260, 314, (byte) 0, TeleportAnimation.FADE_OUT_BEAM);
	}
}
