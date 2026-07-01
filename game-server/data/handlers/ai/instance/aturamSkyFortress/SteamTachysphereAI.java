package ai.instance.aturamSkyFortress;

import static com.nexora.gameserver.model.DialogAction.SETPRO1;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.DialogPage;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI;

/**
 * @author xTz
 */
@AIName("steam_tachysphere")
public class SteamTachysphereAI extends ActionItemNpcAI {

	public SteamTachysphereAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		final QuestState qs = player.getQuestStateList().getQuestState(player.getRace().equals(Race.ELYOS) ? 18302 : 28302);
		if (qs == null) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), DialogPage.NO_RIGHT.id()));
		} else if (qs.getStatus() != QuestStatus.COMPLETE) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
		} else {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		if (dialogActionId == SETPRO1) {
			final QuestState qs = player.getQuestStateList().getQuestState(player.getRace().equals(Race.ELYOS) ? 18302 : 28302);
			if (qs != null && qs.getStatus() == QuestStatus.COMPLETE) {
				TeleportService.teleportTo(player, 300240000, 175.28925f, 625.1088f, 901.009f, (byte) 33);
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(false, 0, 0, 471, false));
				SkillEngine.getInstance().getSkill(player, 19502, 1, player).useNoAnimationSkill();
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
			}
		}
		return true;
	}
}
