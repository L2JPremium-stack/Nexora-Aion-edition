package com.nexora.gameserver.ai.handler;

import com.nexora.gameserver.ai.AISubState;
import com.nexora.gameserver.ai.AbstractAI;
import com.nexora.gameserver.model.gameobjects.Creature;

/**
 * @author Rolandas, Neon
 */
public class FreezeEventHandler {

	public static void onUnfreeze(AbstractAI<? extends Creature> ai) {
		if (ai.isInSubState(AISubState.FREEZE)) {
			ai.setSubStateIfNot(AISubState.NONE);
			ai.think();
		}
	}

	public static void onFreeze(AbstractAI<? extends Creature> ai) {
		ai.setSubStateIfNot(AISubState.FREEZE);
		ai.think();
	}
}
