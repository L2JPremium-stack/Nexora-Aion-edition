package quest.inggison;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.handlers.HandlerResult;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.QuestService;

/**
 * @author Cheatkiller
 */
public class _11036UncommonRecipe extends AbstractQuestHandler {

	public _11036UncommonRecipe() {
		super(11036);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182206731, questId);
		qe.registerQuestNpc(798955).addOnTalkEvent(questId);
		qe.registerQuestNpc(798956).addOnTalkEvent(questId);
		qe.registerQuestNpc(700610).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int dialogActionId = env.getDialogActionId();
		int targetId = env.getTargetId();

		if (qs == null || qs.isStartable()) {
			if (targetId == 0) {
				if (dialogActionId == QUEST_ACCEPT_1) {
					QuestService.startQuest(env);
					return closeDialogWindow(env);
				}
			} else if (targetId == 700610) {
				Npc npc = (Npc) env.getVisibleObject();
				giveQuestItem(env, 182206731, 1);
				npc.getController().deleteAndScheduleRespawn();
				return true;
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 798955) {
				if (dialogActionId == QUEST_SELECT) {
					return sendQuestDialog(env, 1352);
				} else if (dialogActionId == SETPRO1) {
					qs.setQuestVar(1);
					return defaultCloseDialog(env, 1, 1, true, false);
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798956) {
				if (dialogActionId == USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				}
				removeQuestItem(env, 182206731, 1);
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
			return HandlerResult.fromBoolean(sendQuestDialog(env, 4));
		}
		return HandlerResult.FAILED;
	}
}
