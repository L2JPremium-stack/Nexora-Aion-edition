package ai.instance.beshmundirTemple;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.HpPhases;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI;

/**
 * @author xTz
 */
@AIName("manadar")
public class ManadarAI extends AggressiveNpcAI implements HpPhases.PhaseHandler {

	private final HpPhases hpPhases = new HpPhases(90);

	public ManadarAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		hpPhases.tryEnterNextPhase(this);
	}

	@Override
	public void handleHpPhase(int phaseHpPercent) {
		trySpawnTraps();
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		hpPhases.reset();
	}

	private void trySpawnTraps() {
		if (getPosition().isSpawned() && !isDead() && hpPhases.getCurrentPhase() > 0) {
			for (int i = 0; i < 5; i++) {
				rndSpawnInRange(Rnd.nextBoolean() ? 281545 : 281756, 4, 11);
			}
			ThreadPoolManager.getInstance().schedule(this::trySpawnTraps, 6000);
		}
	}
}
