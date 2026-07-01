package ai;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.controllers.attack.AggroTarget;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;

/**
 * Created by Yeats on 20.02.2016.
 */
@AIName("aggressive_boss_summon")
public class AggressiveBossSummonNpcAI extends AggressiveNpcAI {

	public AggressiveBossSummonNpcAI(Npc owner) {
		super(owner);
	}

	@Override
	public void handleAttackComplete() {
		super.handleAttackComplete();
		if (!isCreatorStillFighting())
			getOwner().getController().delete();
	}

	@Override
	public void handleFinishAttack() {
		getOwner().getController().delete();
	}

	private boolean isCreatorStillFighting() {
		return getKnownList().getObject(getCreatorId()) instanceof Creature creator && !creator.isDead() && creator.getAggroList().getTarget(AggroTarget.MOST_HATED) != null;
	}

	@Override
	public void handleDied() {
		super.handleDied();
		getOwner().getController().delete();
	}
}
