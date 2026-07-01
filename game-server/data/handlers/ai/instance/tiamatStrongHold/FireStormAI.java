package ai.instance.tiamatStrongHold;

import java.util.concurrent.Future;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Cheatkiller
 */
@AIName("tahabatafirestorm")
public class FireStormAI extends NpcAI {

	private Future<?> task;

	public FireStormAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		final int skill = getNpcId() == 283102 ? 20753 : 20759; // 4.0
		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> AIActions.useSkill(FireStormAI.this, skill), 0, 1000);

		if (getNpcId() != 283102) // 4.0
			despawn();
	}

	private void despawn() {
		ThreadPoolManager.getInstance().schedule(() -> getOwner().getController().delete(), 20000);
	}

	@Override
	public void handleDespawned() {
		task.cancel(true);
		super.handleDespawned();
	}

	@Override
	public boolean ask(AIQuestion question) {
		return switch (question) {
			case ALLOW_DECAY, ALLOW_RESPAWN, REWARD_AP_XP_DP_LOOT -> false;
			default -> super.ask(question);
		};
	}
}
