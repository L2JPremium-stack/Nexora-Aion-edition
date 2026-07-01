package ai.instance.nightmareCircus;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.services.DialogService;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI;

/**
 * @author Ritsu
 */
@AIName("nightmareharlequin")
public class NightmareHarlequinAI extends GeneralNpcAI {

	public NightmareHarlequinAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		// without an deny dialog we have to refrain from sending dialogs if the player is not allowed to talk to the npc. maybe it's invisible on retail?
		if (DialogService.isInteractionAllowed(player, getOwner()))
			super.handleDialogStart(player);
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogActionId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		if (QuestEngine.getInstance().onDialog(env) && dialogActionId != SETPRO1) {
			return true;
		}
		if (dialogActionId == SETPRO1) {
			SkillEngine.getInstance().getSkill(getOwner(), player.getRace() == Race.ELYOS ? 21470 : 21472, 1, player).useWithoutPropSkill();
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		} else if (dialogActionId == QUEST_SELECT && questId != 0) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10, questId));
		}
		return true;
	}

}
