package ai.worlds.inggison;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.Effect;

import ai.GeneralNpcAI;

/**
 * @author Cheatkiller
 */
@AIName("hugeegg")
public class HugeEggAI extends GeneralNpcAI {

	public HugeEggAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	public float modifyDamage(Creature attacker, float damage, Effect effect) {
		return 1;
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		if (Rnd.nextBoolean()) {
			spawn(217097, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
			AIActions.deleteOwner(this);
		}
	}
}
