package quest.enshar;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.QuestService;
import com.nexora.gameserver.world.WorldMapType;

/**
 * Go to Enshar and talk with Cogelhogan.
 * Talk with Haldor.
 * Order: Go to Enshar and meet with Cogelhogan.
 * 
 * @author Majka
 */
public class _20500EnsharExpedition extends AbstractQuestHandler {

	public _20500EnsharExpedition() {
		super(20500);
	}

	@Override
	public void register() {
		// Cogelhogan 804718
		// Haldor 804719
		int[] npcs = { 804718, 804719 };
		for (int npc : npcs)
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		qe.registerOnLevelChanged(questId);
		qe.registerOnEnterWorld(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;

		int targetId = env.getTargetId();
		int dialogActionId = env.getDialogActionId();

		switch (targetId) {
			case 804718: // Cogelhogan
				if (qs.getStatus() == QuestStatus.START) { // Step 0: Go to Enshar and talk with Cogelhogan.
					if (dialogActionId == QUEST_SELECT) {
						return sendQuestDialog(env, 1011);
					}

					if (dialogActionId == SET_SUCCEED) {
						qs.setQuestVar(1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
				break;
			case 804719: // Haldor
				if (qs.getStatus() == QuestStatus.REWARD) { // Step 1: Talk with Haldor.
					if (dialogActionId == USE_OBJECT) {
						return sendQuestDialog(env, 10002);
					}
					return sendQuestEndDialog(env);
				}
				break;
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		if (player.getWorldId() == WorldMapType.ENSHAR.getId() && !player.getQuestStateList().hasQuest(questId))
			return QuestService.startQuest(env);
		return false;
	}

	@Override
	public void onLevelChangedEvent(Player player) {
		onEnterWorldEvent(new QuestEnv(null, player, questId));
	}
}
