package quest.event_quests;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;

/**
 * @author Bobobear
 */
public class _80331TransformWithTheMagicCane extends AbstractQuestHandler {

	public _80331TransformWithTheMagicCane() {
		super(80331);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(831531).addOnQuestStart(questId);
		qe.registerQuestNpc(831531).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int dialogActionId = env.getDialogActionId();
		int targetId = env.getTargetId();

		if (qs == null || qs.isStartable()) {
			if (targetId == 831531) {
				switch (dialogActionId) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1011);
					case QUEST_ACCEPT_1:
					case QUEST_ACCEPT_SIMPLE:
						return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 831531)
				switch (dialogActionId) {
					case QUEST_SELECT:
					case SELECT_QUEST_REWARD:
						changeQuestStep(env, 0, 0, true); // reward
						return sendQuestDialog(env, 5);
				}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 831531)
				return sendQuestEndDialog(env);
		}
		return false;
	}
}
