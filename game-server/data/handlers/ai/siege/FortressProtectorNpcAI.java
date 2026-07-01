package ai.siege;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.configs.main.SiegeConfig;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.model.templates.npc.NpcRating;
import com.nexora.gameserver.skillengine.model.Effect;

/**
 * @author ATracer, Source
 */
@AIName("fortress_protector")
public class FortressProtectorNpcAI extends AbstractSiegeProtectorAI {

	public FortressProtectorNpcAI(Npc owner) {
		super(owner);
	}

	@Override
	public float modifyOwnerDamage(float damage, Creature effected, Effect effect) {
		if (effected instanceof Npc)
			return damage * 5;
		return damage;
	}

	@Override
	public void modifyOwnerStat(Stat2 stat) {
		if (stat.getStat() == StatEnum.MAXHP && getOwner().getRating() == NpcRating.LEGENDARY)
			stat.setBaseRate(SiegeConfig.FORTRESS_PROTECTOR_HEALTH_MULTIPLIER);
	}
}
