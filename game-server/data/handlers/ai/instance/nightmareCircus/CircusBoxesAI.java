package ai.instance.nightmareCircus;

import java.util.concurrent.Future;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Ritsu
 */
@AIName("circusboxes")
public class CircusBoxesAI extends NpcAI {

	private Future<?> spawnJesterTask;

	public CircusBoxesAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		spawnJester();
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelspawnJesterTask();
		despawnNpcs(233462, 233463);
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		PacketSendUtility.broadcastMessage(getOwner(), 1501144);
		cancelspawnJesterTask();
	}

	private void cancelspawnJesterTask() {
		if (spawnJesterTask != null && !spawnJesterTask.isDone()) {
			spawnJesterTask.cancel(true);
		}
	}

	private void spawnJester() {
		spawnJesterTask = ThreadPoolManager.getInstance().schedule(() -> {
			if (!isDead()) {
				getOwner().getController().delete();
				int count = Rnd.get(3, 5);
				for (int i = 0; i < count; i++) {
					switch (getOwner().getNpcId()) {
						case 831348:
							rndSpawnInRange(233462, 3, 8);
							break;
						case 831349:
							rndSpawnInRange(233463, 3, 8);
							break;
						default:
							return;
					}
				}
			}
		}, 33000);
	}

	private void despawnNpcs(int... npcIds) {
		for (Npc npc : getOwner().getPosition().getWorldMapInstance().getNpcs(npcIds)) {
			npc.getController().delete();
		}
	}
}
