package quest.event_quests;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.QuestService;

public class _80008PieceOfCake extends AbstractQuestHandler {

	public _80008PieceOfCake() {
		super(80008);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798415).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();

		if (env.getTargetId() == 0) {
			if (env.getDialogActionId() == QUEST_ACCEPT_1) {
				QuestService.startEventQuest(env, QuestStatus.START);
				closeDialogWindow(env);
				return true;
			}
		}

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			if (env.getTargetId() == 798415) {
				switch (env.getDialogActionId()) {
					case QUEST_SELECT:
						if (var == 0)
							return sendQuestDialog(env, 2375);
						return false;
					case SELECT_QUEST_REWARD:
						removeQuestItem(env, 182214006, 1);
						return defaultCloseDialog(env, 0, 1, true, true);
				}
			}
		}
		return sendQuestRewardDialog(env, 798415, 0);
	}
}
