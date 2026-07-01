package ai.worlds;

import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;

import ai.GeneralNpcAI;

/**
 * @author Tibald
 */
@AIName("namelessgrave")
public class NamelessGraveAI extends GeneralNpcAI {

	private AtomicBoolean isSpawned = new AtomicBoolean(false);

	public NamelessGraveAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		if (isSpawned.compareAndSet(false, true)) {
			rndSpawnInRange(283905, 1, 2);
			rndSpawnInRange(283905, 1, 2);
		}
	}
}
