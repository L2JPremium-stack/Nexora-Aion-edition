package ai;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.ai.handler.MoveEventHandler;
import com.nexora.gameserver.ai.handler.ThinkEventHandler;
import com.nexora.gameserver.model.CreatureType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.Effect;

@AIName("no_interaction")
public class NoInteractionAI extends NpcAI {

	public NoInteractionAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleBeforeSpawned() {
		super.handleBeforeSpawned();
		getOwner().overrideNpcType(CreatureType.PEACE);
	}

	@Override
	public boolean canThink() {
		return false; // minimal thinking to just support walkers
	}

	@Override
	public void think() {
		ThinkEventHandler.onThink(this);
	}

	@Override
	protected void handleMoveArrived() {
		super.handleMoveArrived();
		MoveEventHandler.onMoveArrived(this);
	}

	@Override
	public float modifyDamage(Creature attacker, float damage, Effect effect) {
		return 0;
	}
}
