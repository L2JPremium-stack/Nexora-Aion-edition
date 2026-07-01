package ai.siege;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.configs.main.SiegeConfig;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.siege.SiegeNpc;
import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.services.siege.BalaurAssaultService;
import com.nexora.gameserver.services.siege.FortressAssault;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Luzien, Estrayl
 */
@AIName("dredgion_commander")
public class DredgionCommanderAI extends SiegeNpcAI {

	private Npc fortressBoss;

	public DredgionCommanderAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(this::findFortressBoss, 3000);
	}

	private void findFortressBoss() {
		fortressBoss = getKnownList().stream()
			.filter(knownObject -> knownObject.get() instanceof Npc npc && (npc.getRace() == Race.GCHIEF_LIGHT || npc.getRace() == Race.GCHIEF_DARK))
			.findAny()
			.map(knownObject -> (Npc) knownObject.get())
			.orElse(null);
		if (fortressBoss != null)
			getAggroList().addHate(fortressBoss, 500000);
	}

	@Override
	protected void handleMoveArrived() {
		super.handleMoveArrived();
		if (getOwner().getDistanceToSpawnLocation() >= 25.0d && fortressBoss != null) {
			getAggroList().addHate(fortressBoss, 1000000);
			AIActions.targetCreature(this, fortressBoss);
			getOwner().getMoveController().moveToPoint(fortressBoss.getX(), fortressBoss.getY(), fortressBoss.getZ());
		}
	}

	@Override
	public float modifyOwnerDamage(float damage, Creature effected, Effect effect) {
		if (effected == fortressBoss)
			damage *= SiegeConfig.FORTRESS_PROTECTOR_HEALTH_MULTIPLIER;
		return damage;
	}

	@Override
	public void modifyOwnerStat(Stat2 stat) {
		if (stat.getStat() == StatEnum.MAXHP)
			stat.setBaseRate(SiegeConfig.FORTRESS_PROTECTOR_HEALTH_MULTIPLIER);
	}

	@Override
	protected void handleDespawned() {
		fortressBoss = null;
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		FortressAssault assault = BalaurAssaultService.getInstance().getFortressAssaultBySiegeId(((SiegeNpc) getOwner()).getSiegeId());
		if (assault != null)
			assault.onDredgionCommanderKilled();

		fortressBoss = null;
		super.handleDied();
	}
}
