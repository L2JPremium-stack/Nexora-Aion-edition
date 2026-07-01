package ai.worlds.brusthonin;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.HpPhases;
import com.nexora.gameserver.controllers.observer.DeathObserver;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI;

/**
 * @author Cheatkiller, Neon
 */
@AIName("unfaithfulntuamu")
public class UnfaithfulNtuamuAI extends AggressiveNpcAI implements HpPhases.PhaseHandler {

	private final HpPhases hpPhases = new HpPhases(50);

	public UnfaithfulNtuamuAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		hpPhases.tryEnterNextPhase(this);
	}

	@Override
	public void handleHpPhase(int phaseHpPercent) {
		Npc ntuamu = getOwner();
		Npc vampireQueen = (Npc) spawn(214583, ntuamu.getX(), ntuamu.getY(), ntuamu.getZ(), ntuamu.getHeading());
		vampireQueen.getLifeStats().setCurrentHpPercent(phaseHpPercent);
		vampireQueen.getObserveController().attach(new DeathObserver(_ ->  AIActions.scheduleRespawn(this)));
		AIActions.deleteOwner(this);
	}
}
