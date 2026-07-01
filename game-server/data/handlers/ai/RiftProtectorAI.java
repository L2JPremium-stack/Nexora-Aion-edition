package ai;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.container.StatEnum;

/**
 * @author Estrayl
 */
@AIName("rift_protector")
public class RiftProtectorAI extends AggressiveNpcAI {

	public RiftProtectorAI(Npc owner) {
		super(owner);
	}

	@Override
	public void modifyOwnerStat(Stat2 stat) {
		if (stat.getStat() == StatEnum.MAXHP)
			stat.setBaseRate(0.1f);
	}
}
