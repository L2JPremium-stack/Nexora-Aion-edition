package ai.instance.idgelDome;

import java.util.concurrent.Future;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Ritsu, Estrayl
 */
@AIName("repelling_flame_cannon")
public class RepellingFlameCannonAI extends NpcAI {

	private Future<?> skillTask;

	public RepellingFlameCannonAI(Npc owner) {
		super(owner);
	}

	@Override
	public float modifyDamage(Creature attacker, float damage, Effect effect) {
		return 0;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> AIActions.useSkill(this, 21648), 1000, 1000);
	}

	@Override
	protected void handleDespawned() {
		skillTask.cancel(true);
		super.handleDespawned();
	}
}
