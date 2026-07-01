package quest.silentera_canyon;

import static com.nexora.gameserver.model.DialogAction.QUEST_SELECT;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;

/**
 * @author zhkchi
 */
public class _30151SnufftheSunsucker extends AbstractQuestHandler {

	public _30151SnufftheSunsucker() {
		super(30151);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799383).addOnQuestStart(questId);
		qe.registerQuestNpc(799383).addOnTalkEvent(questId);
		qe.registerOnKillInWorld(600010000, questId);
	}

	@Override
	public boolean onKillInWorldEvent(QuestEnv env) {
		if (env.getVisibleObject() instanceof Player) {
			Player killed = ((Player) env.getVisibleObject());
			if ((killed.getLevel() + 9) >= env.getPlayer().getLevel() || (killed.getLevel() - 5) <= env.getPlayer().getLevel()) {
				return defaultOnKillRankedEvent(env, 0, 7, true);
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getTargetId() == 799383) {
			if (qs == null || qs.isStartable()) {
				if (env.getDialogActionId() == QUEST_SELECT)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			} else if (qs.getStatus() == QuestStatus.REWARD) {
				if (env.getDialogActionId() == QUEST_SELECT)
					return sendQuestDialog(env, 1352);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}

}
