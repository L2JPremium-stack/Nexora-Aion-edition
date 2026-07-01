package ai;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.handler.AggroEventHandler;
import com.nexora.gameserver.ai.handler.CreatureEventHandler;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 */
@AIName("aggressive")
public class AggressiveNpcAI extends GeneralNpcAI {

	public AggressiveNpcAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleCreatureSee(Creature creature) {
		CreatureEventHandler.onCreatureSee(this, creature);
	}

	@Override
	protected void handleCreatureAggro(Creature creature) {
		if (canThink())
			AggroEventHandler.onAggro(this, creature);
	}

	@Override
	protected boolean handleCreatureNeedsSupportByGuard(Creature creature) {
		return AggroEventHandler.onCreatureNeedsSupportByGuard(this, creature);
	}

}
