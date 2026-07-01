package com.nexora.gameserver.ai.handler;

import java.util.List;

import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.skill.NpcSkillEntry;
import com.nexora.gameserver.skillengine.SkillEngine;

/**
 * @author ATracer
 */
public class SpawnEventHandler {

	public static void onSpawn(NpcAI npcAI) {
		if (npcAI.setStateIfNot(AIState.IDLE)) {
			npcAI.think();
			Npc npc = npcAI.getOwner();
			List<NpcSkillEntry> skills = npc.getSkillList().getPostSpawnSkills();
			if (!skills.isEmpty())
				skills.forEach(s -> SkillEngine.getInstance().getSkill(npc, s.getSkillId(), s.getSkillLevel(), npc).useWithoutPropSkill());
		}
	}

	public static void onDespawn(NpcAI npcAI) {
		npcAI.setStateIfNot(AIState.DESPAWNED);
	}

	public static void onBeforeSpawn(NpcAI npcAI) {
	}

}
