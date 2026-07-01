package ai.events.iceFestival;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestEnv;

import ai.GeneralNpcAI;

@AIName("atirunerk")
public class AtirunerkAI extends GeneralNpcAI {

	public AtirunerkAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		super.handleDialogStart(player);
		QuestEngine.getInstance().onDialog(new QuestEnv(getOwner(), player, 80719, DialogAction.QUEST_SELECT));
	}
}
