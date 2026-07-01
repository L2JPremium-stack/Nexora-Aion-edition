package com.nexora.gameserver.questEngine.handlers.template;

import static com.nexora.gameserver.model.DialogAction.QUEST_SELECT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.handlers.models.Monster;
import com.nexora.gameserver.questEngine.handlers.models.xmlQuest.events.OnKillEvent;
import com.nexora.gameserver.questEngine.handlers.models.xmlQuest.events.OnTalkEvent;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;

/**
 * @author Mr.Poke, Bobobear, Pad
 */
public class XmlQuest extends AbstractTemplateQuestHandler {

	private final Set<Integer> startNpcIds = new HashSet<>();
	private final Set<Integer> endNpcIds = new HashSet<>();
	private final List<OnTalkEvent> onTalkEvents = new ArrayList<>();
	private final List<OnKillEvent> onKillEvents = new ArrayList<>();
	private final boolean isDataDriven;

	public XmlQuest(int questId, List<Integer> startNpcIds, List<Integer> endNpcIds, List<OnTalkEvent> onTalkEvents, List<OnKillEvent> onKillEvents) {
		super(questId);
		if (startNpcIds != null)
			this.startNpcIds.addAll(startNpcIds);
		if (endNpcIds != null)
			this.endNpcIds.addAll(endNpcIds);
		else
			this.endNpcIds.addAll(this.startNpcIds);
		if (onTalkEvents != null)
			this.onTalkEvents.addAll(onTalkEvents);
		if (onKillEvents != null)
			this.onKillEvents.addAll(onKillEvents);
		isDataDriven = DataManager.QUEST_DATA.getQuestById(questId).isDataDriven();
	}

	@Override
	public void register() {
		for (Integer startNpcId : startNpcIds) {
			qe.registerQuestNpc(startNpcId).addOnQuestStart(questId);
			qe.registerQuestNpc(startNpcId).addOnTalkEvent(questId);
		}
		if (!endNpcIds.equals(startNpcIds)) {
			for (Integer endNpcId : endNpcIds) {
				qe.registerQuestNpc(endNpcId).addOnTalkEvent(questId);
			}
		}
		for (OnTalkEvent onTalkEvent : onTalkEvents) {
			for (Integer npcId : onTalkEvent.getIds()) {
				qe.registerQuestNpc(npcId).addOnTalkEvent(questId);
			}
		}
		for (OnKillEvent onKillEvent : onKillEvents) {
			for (Monster monster : onKillEvent.getMonsters()) {
				for (Integer monsterId : monster.getNpcIds()) {
					qe.registerQuestNpc(monsterId).addOnKillEvent(questId);
				}
			}
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		//env.setQuestId(questId);
		for (OnTalkEvent onTalkEvent : onTalkEvents) {
			if (onTalkEvent.operate(env))
				return true;
		}

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		
		if (qs == null || qs.isStartable()) {
			if (startNpcIds.contains(targetId)) {
				if (env.getDialogActionId() == QUEST_SELECT)
					return sendQuestDialog(env, isDataDriven ? 4762 : 1011);
				else
					return sendQuestStartDialog(env);
			}
		} else if (qs.getStatus() == QuestStatus.REWARD && endNpcIds.contains(targetId)) {
			return sendQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		//env.setQuestId(questId);
		for (OnKillEvent onKillEvent : onKillEvents) {
			if (onKillEvent.operate(env))
				return true;
		}
		return false;
	}
}
