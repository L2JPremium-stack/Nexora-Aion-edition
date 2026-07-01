package com.nexora.gameserver.ai.handler;

import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 */
public class ActivateEventHandler {

	public static void onActivate(NpcAI npcAI) {
		if (npcAI.isInState(AIState.IDLE)) {
			npcAI.getOwner().updateKnownlist();
			npcAI.think();
		}
	}

	public static void onDeactivate(NpcAI npcAI) {
		npcAI.think();
		Npc npc = npcAI.getOwner();
		npc.updateKnownlist();
		npc.getController().loseAggro(false);
		if (npcAI.ask(AIQuestion.REMOVE_EFFECTS_ON_MAP_REGION_DEACTIVATE))
			npc.getEffectController().removeAllEffects();
	}
}
