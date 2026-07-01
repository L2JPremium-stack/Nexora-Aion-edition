package ai.instance.danuarSanctuary;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.Effect;

import ai.GeneralNpcAI;

/**
 * @author Tibald
 */
@AIName("ancientdanuarcoffin")
public class AncientDanuarCoffinAI extends GeneralNpcAI {

	public AncientDanuarCoffinAI(Npc owner) {
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
		if (Rnd.chance() < 40) {
			spawn(233085, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
		}
	}
}
