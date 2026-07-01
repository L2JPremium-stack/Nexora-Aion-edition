package quest.event_quests;

import static com.nexora.gameserver.model.DialogAction.*;

import java.util.List;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.quest.QuestItems;
import com.nexora.gameserver.model.templates.rewards.BonusType;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.handlers.HandlerResult;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.QuestService;

/**
 * @author Rolandas
 */
public class _80016EventSockHop extends AbstractQuestHandler {

	public _80016EventSockHop() {
		super(80016);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799763).addOnQuestStart(questId);
		qe.registerQuestNpc(799763).addOnTalkEvent(questId);
		qe.registerOnBonusApply(questId, BonusType.MOVIE);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.isStartable()) {
			if (env.getTargetId() == 799763) {
				switch (env.getDialogActionId()) {
					case USE_OBJECT:
						return sendQuestDialog(env, 1011);
					case QUEST_ACCEPT_1:
						QuestService.startEventQuest(env, QuestStatus.START);
						return sendQuestDialog(env, 1003);
					default:
						return sendQuestStartDialog(env);
				}
			}
			return false;
		}

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			if (env.getTargetId() == 799763) {
				switch (env.getDialogActionId()) {
					case USE_OBJECT:
					case QUEST_SELECT:
						if (var == 0)
							return sendQuestDialog(env, 2375);
						return false;
					case CHECK_USER_HAS_QUEST_ITEM:
						return checkQuestItems(env, 0, 1, true, 5, 2716);
				}
			}
		}

		return sendQuestRewardDialog(env, 799763, 0);
	}

	@Override
	public HandlerResult onBonusApplyEvent(QuestEnv env, BonusType bonusType, List<QuestItems> rewardItems) {
		if (bonusType != BonusType.MOVIE || env.getQuestId() != questId)
			return HandlerResult.UNKNOWN;

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (qs.getCompleteCount() == 9) { // [Event] Hat Box
				rewardItems.add(new QuestItems(188051106, 1));
			}
			// randomize movie
			playQuestMovie(env, Rnd.nextBoolean() ? 103 : 104);
			return HandlerResult.SUCCESS;
		}
		return HandlerResult.FAILED;
	}

}
