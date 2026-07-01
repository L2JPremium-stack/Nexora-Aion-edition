package ai.events.iceFestival;

import java.util.List;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI;

@AIName("ishrunerk")
public class IshrunerkAI extends GeneralNpcAI {

	public IshrunerkAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		super.handleDialogStart(player);
		// custom dialog handling because the quests don't show up in the quest selection (incomplete quest data in the 4.8 game client)
		for (int questId : List.of(80715, 80716, 80717, 80718, 80720)) {
			if (QuestEngine.getInstance().onDialog(new QuestEnv(getOwner(), player, questId, DialogAction.QUEST_SELECT)))
				break;
		}
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		if (dialogActionId == DialogAction.SELECTED_QUEST_NOREWARD) {
			QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogActionId);
			env.setExtendedRewardIndex(extendedRewardIndex);
			if (QuestEngine.getInstance().onDialog(env))
				PacketSendUtility.sendPacket(env.getPlayer(), new SM_DIALOG_WINDOW(getObjectId(), 0, questId)); // close window
			return true;
		}
		return super.onDialogSelect(player, dialogActionId, questId, extendedRewardIndex);
	}
}
