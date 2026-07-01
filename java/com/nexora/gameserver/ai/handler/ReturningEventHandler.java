package com.nexora.gameserver.ai.handler;

import java.util.List;

import com.nexora.gameserver.ai.AILogger;
import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.ai.AISubState;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.ai.event.AIEventType;
import com.nexora.gameserver.ai.manager.EmoteManager;
import com.nexora.gameserver.ai.manager.WalkManager;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.skill.NpcSkillEntry;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.skillengine.model.DispelSlotType;

/**
 * @author ATracer
 */
public class ReturningEventHandler {

	/**
	 * @param npcAI
	 */
	public static void onNotAtHome(NpcAI npcAI) {
		if (npcAI.isLogging()) {
			AILogger.info(npcAI, "onNotAtHome");
		}
		if (!npcAI.isMoveSupported()) {
			npcAI.onGeneralEvent(AIEventType.BACK_HOME);
		} else if (npcAI.setStateIfNot(AIState.RETURNING)) {
			npcAI.setSubStateIfNot(AISubState.NONE);
			if (npcAI.isLogging()) {
				AILogger.info(npcAI, "returning and restoring");
			}
			Npc npc = npcAI.getOwner();
			EmoteManager.emoteStartReturning(npc);
			if (npc.isPathWalker() && WalkManager.startWalking(npcAI))
				return;
			npc.getMoveController().returnToLastStepOrSpawn();
		}
	}

	/**
	 * @param npcAI
	 */
	public static void onBackHome(NpcAI npcAI) {
		if (npcAI.isLogging()) {
			AILogger.info(npcAI, "onBackHome");
		}
		npcAI.getOwner().getMoveController().clearBackSteps();
		if (npcAI.setStateIfNot(AIState.IDLE)) {
			npcAI.setSubStateIfNot(AISubState.NONE);
			npcAI.getOwner().getEffectController().removeByDispelSlotType(DispelSlotType.BUFF);
			EmoteManager.emoteStartIdling(npcAI.getOwner());
			npcAI.think();
			Npc npc = npcAI.getOwner();
			List<NpcSkillEntry> skills = npc.getSkillList().getPostSpawnSkills();
			if (!skills.isEmpty())
				skills.forEach(s -> SkillEngine.getInstance().getSkill(npc, s.getSkillId(), s.getSkillLevel(), npc).useWithoutPropSkill());
		}
		npcAI.getOwner().getPosition().getWorldMapInstance().getInstanceHandler().onBackHome(npcAI.getOwner());
	}
}
