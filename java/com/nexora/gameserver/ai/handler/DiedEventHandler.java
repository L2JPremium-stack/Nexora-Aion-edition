package com.nexora.gameserver.ai.handler;

import com.nexora.gameserver.ai.AILogger;
import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.ai.AISubState;
import com.nexora.gameserver.ai.NpcAI;

/**
 * @author ATracer
 */
public class DiedEventHandler {

	public static void onDie(NpcAI npcAI) {
		if (npcAI.isLogging()) {
			AILogger.info(npcAI, "onDie");
		}

		ShoutEventHandler.onDied(npcAI);

		npcAI.setStateIfNot(AIState.DIED);
		npcAI.setSubStateIfNot(AISubState.NONE);
		npcAI.getOwner().setTarget(null);
	}

}
