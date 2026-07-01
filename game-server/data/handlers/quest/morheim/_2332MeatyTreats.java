package quest.morheim;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.model.DialogPage;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.QuestService;

/**
 * @author stpavel, Neon
 */
public class _2332MeatyTreats extends AbstractQuestHandler {

	public _2332MeatyTreats() {
		super(2332);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798084).addOnQuestStart(questId);
		qe.registerQuestNpc(798084).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.isStartable()) {
			if (targetId == 798084) {
				if (env.getDialogActionId() == QUEST_SELECT)
					return sendQuestDialog(env, 4762);
				else
					return sendQuestStartDialog(env);
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 798084) {
				switch (env.getDialogActionId()) {
					case QUEST_SELECT:
						if (QuestService.collectItemCheck(env, false))
							return sendQuestDialog(env, 1352);
						else
							return sendQuestDialog(env, 1693);
					case SETPRO1:
					case SETPRO2:
					case SETPRO3:
						if (QuestService.collectItemCheck(env, true)) {
							qs.setQuestVar(1);
							qs.setRewardGroup(env.getDialogActionId() - SETPRO1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, DialogPage.getRewardPageByIndex(qs.getRewardGroup()).id());
						}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD && targetId == 798084) {
			return sendQuestEndDialog(env);
		}
		return false;
	}
}
