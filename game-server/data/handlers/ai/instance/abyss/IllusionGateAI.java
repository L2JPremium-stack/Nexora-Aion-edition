package ai.instance.abyss;

import java.util.concurrent.Future;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * Created on June 24th, 2016
 *
 * @author Estrayl
 * @since AION 4.8
 */
@AIName("illusion_gate")
public class IllusionGateAI extends NpcAI {

	private Future<?> spawnTask;

	public IllusionGateAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		spawnTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> {
			for (int i = 0; i < 2; i++) {
				rndSpawnInRange(getNpcId() + Rnd.get(1, 3), 0, 1);
			}
		}, 10000, 30000);
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		cancelTask();
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelTask();
	}

	private void cancelTask() {
		if (spawnTask != null && !spawnTask.isCancelled())
			spawnTask.cancel(true);
	}
}
