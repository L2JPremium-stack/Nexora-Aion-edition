package ai;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author xTz, Neon
 */
@AIName("onedmg_passive")
public class OneDmgNoActionAI extends NpcAI {

	public OneDmgNoActionAI(Npc owner) {
		super(owner);
	}

	@Override
	public float modifyDamage(Creature attacker, float damage, Effect effect) {
		return 1;
	}

	@Override
	public void modifyOwnerStat(Stat2 stat) {
		switch (stat.getStat()) { // ai owner should not evade or resist
			case MAGICAL_RESIST:
			case EVASION:
				stat.setBase(0);
				stat.setBonus(0);
		}
	}
}
