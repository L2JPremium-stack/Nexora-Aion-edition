package ai.siege;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.configs.main.SiegeConfig;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.container.StatEnum;

/**
 * @author ATracer
 */
@AIName("artifact_protector")
public class ArtifactProtectorAI extends AbstractSiegeProtectorAI {

	public ArtifactProtectorAI(Npc owner) {
		super(owner);
	}

	@Override
	public void modifyOwnerStat(Stat2 stat) {
		if (stat.getStat() == StatEnum.MAXHP && getOwner().getLevel() >= 65)
			stat.setBaseRate(SiegeConfig.ARTIFACT_PROTECTOR_HEALTH_MULTIPLIER);
	}
}
