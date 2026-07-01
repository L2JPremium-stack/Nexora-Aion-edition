package quest.tiamat_stronghold;

import static com.nexora.gameserver.model.DialogAction.QUEST_SELECT;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestActionType;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;

/**
 * @author Estrayl
 */
public class _30760PetrifiedHeroOfTheAsmodians extends AbstractQuestHandler {

	private static final int QUEST_ITEM_ID = 182213268; // Marchutan's Protection
	private static final int START_END_NPC_ID = 804871; // Hank
	private static final int STATUE_NPC_ID = 701499; // Asmodian Hero's Statue

	public _30760PetrifiedHeroOfTheAsmodians() {
		super(30760);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(START_END_NPC_ID).addOnQuestStart(questId);
		qe.registerQuestNpc(START_END_NPC_ID).addOnTalkEvent(questId);
		qe.registerCanAct(questId, STATUE_NPC_ID);
		qe.registerQuestNpc(STATUE_NPC_ID).addOnTalkEvent(questId);
		qe.registerQuestItem(QUEST_ITEM_ID, questId);
	}

	public boolean onDialogEvent(QuestEnv env) {
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		int dialogActionId = env.getDialogActionId();
		int targetId = env.getTargetId();

		if (qs == null || qs.isStartable()) {
			if (targetId == START_END_NPC_ID) {
				if (dialogActionId == QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env, QUEST_ITEM_ID, 1);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == STATUE_NPC_ID) {
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == START_END_NPC_ID) {
				if (dialogActionId == QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onCanAct(QuestEnv env, QuestActionType questEventType, Object... objects) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		return qs != null && qs.getStatus() == QuestStatus.START && env.getTargetId() == STATUE_NPC_ID
			&& player.getInventory().getItemCountByItemId(QUEST_ITEM_ID) > 0;
	}
}
