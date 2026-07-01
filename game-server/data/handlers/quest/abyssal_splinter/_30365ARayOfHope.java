//Wird der Titel Clientseitig vergeben? Konnte auch in anderen Scripts nichts zur Titelvergabe finden...

package quest.abyssal_splinter;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.handlers.HandlerResult;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.QuestService;

/**
 * @author Rikka
 */
public class _30365ARayOfHope extends AbstractQuestHandler {

	public _30365ARayOfHope() {
		super(30365);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204241).addOnTalkEvent(questId);
		qe.registerQuestNpc(203574).addOnTalkEvent(questId);
		qe.registerQuestNpc(278040).addOnTalkEvent(questId);
		qe.registerQuestItem(182209824, questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int dialogActionId = env.getDialogActionId();
		int targetId = env.getTargetId();

		if (qs == null || qs.isStartable()) {
			return false;
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 204241: // Annemari
					switch (dialogActionId) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1352);
							}
							return false;
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1); // 1
					}
					break;
				case 203574: // Arekedil
					switch (dialogActionId) {
						case QUEST_SELECT:
							if (var == 1) {
								return sendQuestDialog(env, 1693);
							} else if (var == 3) {
								return sendQuestDialog(env, 2375);
							}
							return false;
						case SETPRO2:
							return defaultCloseDialog(env, 1, 2); // 2
						case SELECT_QUEST_REWARD:
							changeQuestStep(env, 3, 3, true); // reward
							return sendQuestEndDialog(env);
					}
					break;
				case 278040: // Haug
					switch (dialogActionId) {
						case QUEST_SELECT:
							if (var == 2) {
								return sendQuestDialog(env, 2034);
							}
							return false;
						case SETPRO3:
							return defaultCloseDialog(env, 2, 3); // 3
					}
					break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203574) { // Arekedil
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.isStartable()) {
			return HandlerResult.fromBoolean(QuestService.startQuest(env));
		}
		return HandlerResult.FAILED;
	}
}
