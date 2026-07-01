package ai.siege;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author Bobobear
 */
@AIName("base_flag")
public class FlagBaseNpcAI extends NpcAI {

	public FlagBaseNpcAI(Npc owner) {
		super(owner);
	}

	@Override
	public float modifyDamage(Creature attacker, float damage, Effect effect) {
		return 0;
	}

	@Override
	public float modifyOwnerDamage(float damage, Creature effected, Effect effect) {
		return 0;
	}
}
