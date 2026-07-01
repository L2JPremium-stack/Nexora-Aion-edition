package quest.reshanta;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.gameserver.model.gameobjects.Npc;
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
public class _14040OrdersFromReshanta extends AbstractQuestHandler {

	public _14040OrdersFromReshanta() {
		super(14040);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(278501).addOnTalkEvent(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnLevelChanged(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if (targetId != 278501)
			return false;
		if (qs.getStatus() == QuestStatus.START) {
			if (env.getDialogActionId() == QUEST_SELECT)
				return sendQuestDialog(env, 10002);
			else if (env.getDialogActionId() == SELECT_QUEST_REWARD) {
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return sendQuestDialog(env, 5);
			}
			return false;
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			return sendQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		if (player.getWorldId() == WorldMapType.RESHANTA.getId() && !player.getQuestStateList().hasQuest(questId))
			return QuestService.startQuest(env);
		return false;
	}

	@Override
	public void onLevelChangedEvent(Player player) {
		onEnterWorldEvent(new QuestEnv(null, player, questId));
	}
}
