package quest.altgard;

import static com.nexora.gameserver.model.DialogAction.QUEST_SELECT;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.QuestService;
import com.nexora.gameserver.world.WorldMapType;

/**
 * @author Artur, Majka
 */
public class _24010SuthransOrders extends AbstractQuestHandler {

	public _24010SuthransOrders() {
		super(24010);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203557).addOnTalkEvent(questId);
		qe.registerOnEnterWorld(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;

		int targetId = env.getTargetId();
		int dialogActionId = env.getDialogActionId();
		if (targetId != 203557)
			return false;
		if (qs.getStatus() == QuestStatus.START) {
			if (dialogActionId == QUEST_SELECT) {
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return sendQuestDialog(env, 1011);
			} else
				return sendQuestStartDialog(env);
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			return sendQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		if (player.getWorldId() == WorldMapType.ALTGARD.getId() && !player.getQuestStateList().hasQuest(questId))
			return QuestService.startQuest(env);
		return false;
	}
}
