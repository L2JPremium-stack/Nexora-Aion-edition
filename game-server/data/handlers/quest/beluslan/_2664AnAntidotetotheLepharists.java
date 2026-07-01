package quest.beluslan;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;

/**
 * @author VladimirZ
 */
public class _2664AnAntidotetotheLepharists extends AbstractQuestHandler {

	private final static int[] npc_ids = { 204777, 700324 };

	public _2664AnAntidotetotheLepharists() {
		super(2664);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204777).addOnQuestStart(questId);
		for (int npc_id : npc_ids)
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 204777) {
			if (qs == null || qs.isStartable()) {
				if (env.getDialogActionId() == QUEST_SELECT)
					return sendQuestDialog(env, 4762);
				else
					return sendQuestStartDialog(env, workItems.getFirst());
			}
		}
		if (qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204777) {
				if (env.getDialogActionId() == USE_OBJECT)
					return sendQuestDialog(env, 10002);
				else if (env.getDialogActionId() == SELECT_QUEST_REWARD)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		} else if (qs.getStatus() != QuestStatus.START) {
			return false;
		}
		if (targetId == 700324) {
			switch (env.getDialogActionId()) {
				case USE_OBJECT:
					if (var >= 0 && var < 4) {
						if (player.getInventory().getItemCountByItemId(182204489) >= 1) {
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return true;
						}
					} else if (var == 4) {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
					return false;
			}
		}
		return false;
	}
}
