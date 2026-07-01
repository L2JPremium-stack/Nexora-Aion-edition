package ai.siege;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.configs.main.SiegeConfig;
import com.nexora.gameserver.controllers.attack.DamageInfo;
import com.nexora.gameserver.model.base.Base;
import com.nexora.gameserver.model.base.BaseOccupier;
import com.nexora.gameserver.model.gameobjects.AionObject;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.model.team.TemporaryPlayerTeam;
import com.nexora.gameserver.model.templates.spawns.basespawns.BaseSpawnTemplate;
import com.nexora.gameserver.services.BaseService;

import ai.AggressiveNpcAI;

/**
 * @author Estrayl
 */
@AIName("base_protector")
public class BaseProtectorAI extends AggressiveNpcAI {

	public BaseProtectorAI(Npc owner) {
		super(owner);
	}

	@Override
	protected BaseSpawnTemplate getSpawnTemplate() {
		return (BaseSpawnTemplate) super.getSpawnTemplate();
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		Base<?> base = BaseService.getInstance().getActiveBase(getSpawnTemplate().getId());
		if (base == null)
			return;
		DamageInfo<AionObject> mostDamage = getAggroList().getFinalDamageList().toTeamDamages().getMostDamage();
		Creature bossKiller = mostDamage.getAttacker() instanceof TemporaryPlayerTeam<?> team ? team.getLeaderObject() : (Creature) mostDamage.getAttacker();
		BaseOccupier newOccupier = base.getOccupier(bossKiller);
		BaseService.getInstance().capture(base.getId(), newOccupier);
	}

	@Override
	public void modifyOwnerStat(Stat2 stat) {
		if (stat.getStat() == StatEnum.MAXHP && getOwner().getLevel() >= 65) // Avoid adjusting low-level zones
			stat.setBaseRate(SiegeConfig.BASE_PROTECTOR_HEALTH_MULTIPLIER);
	}

	@Override
	public boolean ask(AIQuestion question) {
		return switch (question) {
			case ALLOW_DECAY, ALLOW_RESPAWN, REWARD_LOOT, REMOVE_EFFECTS_ON_MAP_REGION_DEACTIVATE -> false;
			default -> super.ask(question);
		};
	}
}
