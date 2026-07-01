package ai.siege;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.configs.main.SiegeConfig;
import com.nexora.gameserver.controllers.attack.AggroTarget;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.siege.SiegeNpc;
import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.SummonerAI;

/**
 * TODO: Aether Concentrators - currently not necessary since they are nearly impossible to use and need
 * about 100 player to be activated by default.
 * 
 * @author Estrayl
 */
@AIName("enraged_agent")
public class EnragedAgent extends SummonerAI {

	public EnragedAgent(Npc owner) {
		super(owner);
	}

	@Override
	public void onEffectEnd(Effect effect) {
		switch (effect.getSkillId()) {
			case 18704:
				ThreadPoolManager.getInstance()
					.schedule(() -> SkillEngine.getInstance().getSkill(getOwner(), 18705, 60, getAggroList().getTarget(AggroTarget.MOST_HATED)).useSkill(), 650);
				break;
		}
	}

	@Override
	public void modifyOwnerStat(Stat2 stat) {
		if (stat.getStat() == StatEnum.MAXHP)
			stat.setBaseRate(SiegeConfig.FORTRESS_PROTECTOR_HEALTH_MULTIPLIER);
	}

	@Override
	public void handleBackHome() {
		super.handleBackHome();
		getAggroList().clear(); // make sure old damages aren't counted in stopSiege
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		AbstractSiegeProtectorAI.stopSiege((SiegeNpc) getOwner());
	}
}
