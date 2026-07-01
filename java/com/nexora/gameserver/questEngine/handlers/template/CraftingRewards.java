package com.nexora.gameserver.questEngine.handlers.template;

import static com.nexora.gameserver.model.DialogAction.QUEST_SELECT;
import static com.nexora.gameserver.model.DialogAction.SELECT_QUEST_REWARD;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.craft.CraftSkillUpdateService;

/**
 * @author Bobobear, Pad
 */
public class CraftingRewards extends AbstractTemplateQuestHandler {

	private final int startNpcId, endNpcId;
	private final int skillId;
	private final int levelReward;
	private final int questMovie;
	private final boolean isDataDriven;

	public CraftingRewards(int questId, int startNpcId, int skillId, int levelReward, int endNpcId, int questMovie) {
		super(questId);
		this.startNpcId = startNpcId;
		this.endNpcId = endNpcId != 0 ? endNpcId : startNpcId;
		this.skillId = skillId;
		this.levelReward = levelReward;
		this.questMovie = questMovie;
		isDataDriven = DataManager.QUEST_DATA.getQuestById(questId).isDataDriven();
	}

	@Override
	public void register() {
		if (startNpcId != 0) {
			qe.registerQuestNpc(startNpcId).addOnQuestStart(questId);
			qe.registerQuestNpc(startNpcId).addOnTalkEvent(questId);
		}
		if (endNpcId != startNpcId) {
			qe.registerQuestNpc(endNpcId).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int dialogActionId = env.getDialogActionId();
		int targetId = env.getTargetId();

		if (qs == null || qs.isStartable()) {
			if (targetId == startNpcId && canLearn(player)) {
				switch (dialogActionId) {
					case QUEST_SELECT:
						return sendQuestDialog(env, isDataDriven ? 4762 : 1011);
					default:
						return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == endNpcId && canLearn(player)) {
				switch (dialogActionId) {
					case QUEST_SELECT:
						return sendQuestDialog(env, isDataDriven ? 1011 : 2375);
					case SELECT_QUEST_REWARD:
						qs.setQuestVar(0);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						player.getSkillList().addSkill(player, skillId, levelReward);
						if (questMovie != 0)
							playQuestMovie(env, questMovie);
						return sendQuestEndDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == endNpcId) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	private boolean canLearn(Player player) {
		if (levelReward == 400)
			return CraftSkillUpdateService.getInstance().canLearnMoreExpertCraftingSkill(player);
		if (levelReward == 500)
			return CraftSkillUpdateService.getInstance().canLearnMoreMasterCraftingSkill(player);
		throw new IllegalStateException("Unhandled levelReward " + levelReward);
	}
}
