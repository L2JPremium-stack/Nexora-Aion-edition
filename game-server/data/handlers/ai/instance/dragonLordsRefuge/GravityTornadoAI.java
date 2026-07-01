package ai.instance.dragonLordsRefuge;

import java.util.concurrent.Future;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Luzien, Estrayl
 */
@AIName("gravity_tornado")
public class GravityTornadoAI extends NpcAI {

	private Future<?> task;

	public GravityTornadoAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> AIActions.useSkill(this, getNpcId() == 283142 ? 20966 : 21901), 2500, 6000);
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
