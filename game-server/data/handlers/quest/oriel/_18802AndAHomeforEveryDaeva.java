package quest.oriel;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.HousingService;

/**
 * @author zhkchi
 */
public class _18802AndAHomeforEveryDaeva extends AbstractQuestHandler {

	public _18802AndAHomeforEveryDaeva() {
		super(18802);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(830005).addOnQuestStart(questId);
		qe.registerQuestNpc(830005).addOnTalkEvent(questId);
		qe.registerQuestNpc(830069).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int dialogActionId = env.getDialogActionId();
		int targetId = env.getTargetId();

		if (qs == null || qs.isStartable()) {
			if (targetId == 830005) {
				switch (dialogActionId) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1011);
					case QUEST_ACCEPT_1:
					case QUEST_ACCEPT_SIMPLE:
						return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 830069:
					switch (dialogActionId) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2375);
						}
						case SELECT_QUEST_REWARD: {
							changeQuestStep(env, 0, 0, true);
							return sendQuestEndDialog(env);
						}
					}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 830069) {
				if (dialogActionId == SELECTED_QUEST_NOREWARD) {
					HousingService.getInstance().registerPlayerStudio(player);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

}
