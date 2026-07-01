package quest.event_quests;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.spawns.SpawnSearchResult;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.utils.PositionUtil;

/**
 * @author Cheatkiller
 */
public class _51008NonHelpingHands extends AbstractQuestHandler {

	public _51008NonHelpingHands() {
		super(51008);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(831039).addOnQuestStart(questId);
		qe.registerQuestNpc(831039).addOnTalkEvent(questId);
		qe.registerQuestNpc(219291).addOnAttackEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int dialogActionId = env.getDialogActionId();
		int targetId = env.getTargetId();

		if (qs == null || qs.isStartable()) {
			if (targetId == 831039) {
				if (dialogActionId == QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 831039) {
				if (dialogActionId == USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onAttackEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int targetId = env.getTargetId();
			int var = qs.getQuestVarById(0);
			if (targetId == 219291) {
				final Npc npc = (Npc) env.getVisibleObject();
				if (!npc.isSpawned())
					return false;
				final SpawnSearchResult searchResult = DataManager.SPAWNS_DATA.getFirstSpawnByNpcId(npc.getWorldId(), 831037);
				if (PositionUtil.getDistance(searchResult.getSpot().getX(), searchResult.getSpot().getY(), searchResult.getSpot().getZ(), npc.getX(), npc.getY(),
					npc.getZ()) <= 15) {
					npc.getController().deleteAndScheduleRespawn();
					if (var == 0)
						changeQuestStep(env, 0, 1);
					else
						changeQuestStep(env, 1, 1, true);
					return true;
				}
			}
		}
		return false;
	}
}
