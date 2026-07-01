package com.nexora.gameserver.ai.manager;

import com.nexora.gameserver.ai.AILogger;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 */
public class FollowManager {

	public static void targetTooFar(NpcAI npcAI) {
		Npc npc = npcAI.getOwner();
		if (npcAI.isLogging()) {
			AILogger.info(npcAI, "Follow manager - targetTooFar");
		}
		if (npcAI.isMoveSupported()) {
			npc.getMoveController().moveToTargetObject();
		}
	}
}
