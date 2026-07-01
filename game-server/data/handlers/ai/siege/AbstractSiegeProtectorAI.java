package ai.siege;

import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.siege.SiegeNpc;
import com.nexora.gameserver.model.siege.SiegeLocation;
import com.nexora.gameserver.services.SiegeService;
import com.nexora.gameserver.services.siege.Siege;

public abstract class AbstractSiegeProtectorAI extends SiegeNpcAI {

	public AbstractSiegeProtectorAI(Npc owner) {
		super(owner);
	}

	@Override
	public void handleBackHome() {
		super.handleBackHome();
		getAggroList().clear(); // make sure old damages aren't counted in stopSiege
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		stopSiege((SiegeNpc) getOwner());
	}

	static void stopSiege(SiegeNpc siegeProtector) {
		Siege<? extends SiegeLocation> siege = SiegeService.getInstance().getSiege(siegeProtector.getSiegeId());
		siegeProtector.getAggroList().stream().forEach(aggroInfo -> siege.getSiegeCounter().addDamage(aggroInfo.getAttacker().getMaster(), aggroInfo.getDamage()));
		siege.setBossKilled(true);
		SiegeService.getInstance().stopSiege(siege.getSiegeLocationId());
	}
}
