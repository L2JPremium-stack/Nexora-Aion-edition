package ai.instance.idgelDome;

import java.util.concurrent.Future;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.SkillTemplate;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Ritsu, Estrayl
 */
@AIName("unstable_id_energy")
public class UnstableIdeEnergyAI extends NpcAI {

	private Future<?> skillTask;

	public UnstableIdeEnergyAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		scheduleSkill(Rnd.get(10000, 30000));
	}

	@Override
	public float modifyDamage(Creature attacker, float damage, Effect effect) {
		return 0;
	}

	private void scheduleSkill(int delay) {
		skillTask = ThreadPoolManager.getInstance().schedule(() -> AIActions.useSkill(this, 21559), delay);
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		switch (skillTemplate.getSkillId()) {
			case 21559:
				scheduleSkill(Rnd.get(10000, 30000));
				break;
		}
	}

	@Override
	protected void handleDespawned() {
		skillTask.cancel(true);
		super.handleDespawned();
	}
}
