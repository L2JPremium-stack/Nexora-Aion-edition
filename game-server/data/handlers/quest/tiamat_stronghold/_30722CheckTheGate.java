package quest.tiamat_stronghold;

import static com.nexora.gameserver.model.DialogAction.QUEST_SELECT;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.world.zone.ZoneName;

/**
 * @author Estrayl
 */
public class _30722CheckTheGate extends AbstractQuestHandler {

	private static final String ZONE_NAME = "DRAGONFALLS_GLARE_220080000";
	private static final int START_NPC_ID = 804868; // Rosalee
	private static final int END_NPC_ID = 804898; // Werine

	public _30722CheckTheGate() {
		super(30722);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(START_NPC_ID).addOnQuestStart(questId);
		qe.registerQuestNpc(END_NPC_ID).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get(ZONE_NAME), questId);
	}

	public boolean onDialogEvent(QuestEnv env) {
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		int dialogActionId = env.getDialogActionId();
		int targetId = env.getTargetId();

		if (qs == null || qs.isStartable()) {
			if (targetId == START_NPC_ID) {
				if (dialogActionId == QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == END_NPC_ID) {
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
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName != ZoneName.get(ZONE_NAME))
			return false;

		Player player = env.getPlayer();
		if (player == null)
			return false;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (qs.getQuestVarById(0) == 0) {
				changeQuestStep(env, 0, 1, true);
				return true;
			}
		}
		return false;
	}

}
