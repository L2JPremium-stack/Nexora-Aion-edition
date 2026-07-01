package com.nexora.gameserver.questEngine.handlers.template;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.QuestService;

/**
 * @author Majka, Bobobear, Pad
 */
public class ReportOnLevelUp extends AbstractTemplateQuestHandler {

	private final Set<Integer> endNpcIds = new HashSet<>();

	public ReportOnLevelUp(int questId, List<Integer> endNpcIds) {
		super(questId);
		if (endNpcIds != null) {
			this.endNpcIds.addAll(endNpcIds);
		}
	}

	@Override
	public void register() {
		for (Integer endNpcId : endNpcIds)
			qe.registerQuestNpc(endNpcId).addOnTalkEvent(questId);

		qe.registerOnEnterWorld(questId);
		qe.registerOnLevelChanged(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();

		if (qs == null)
			return false;
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (endNpcIds.contains(targetId))
				return sendQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		return startQuest(env.getPlayer());
	}

	@Override
	public void onLevelChangedEvent(Player player) {
		startQuest(player);
	}

	private boolean startQuest(Player player) {
		if (!player.getQuestStateList().hasQuest(questId))
			return QuestService.startQuest(new QuestEnv(null, player, questId), QuestStatus.REWARD, false);
		return false;
	}
}
