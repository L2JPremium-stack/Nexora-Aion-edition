package com.nexora.gameserver.ai;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;

/**
 * @author ATracer
 */
public abstract class AIRequest {

	public abstract void acceptRequest(Creature requester, Player responder, int requestId);

	public void denyRequest(Creature requester, Player responder) {
	};
}
