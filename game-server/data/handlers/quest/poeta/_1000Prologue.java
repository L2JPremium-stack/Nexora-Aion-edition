package quest.poeta;

import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.QuestService;

/**
 * @author MrPoke
 */
public class _1000Prologue extends AbstractQuestHandler {

	public _1000Prologue() {
		super(1000);
	}

	@Override
	public void register() {
		qe.registerOnEnterWorld(questId);
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		if (player.getRace() == Race.ELYOS && !player.getQuestStateList().hasQuest(questId)) {
			env.setQuestId(questId);
			if (QuestService.startQuest(env) || player.getQuestStateList().getQuestState(questId).getStatus() == QuestStatus.START) {
				playQuestMovie(env, 1, true);
				return true;
			}
		}
		return false;
	}

	@Override
	public void onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId != 1)
			return;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return;
		qs.setStatus(QuestStatus.REWARD);
		QuestService.finishQuest(env);
	}
}
