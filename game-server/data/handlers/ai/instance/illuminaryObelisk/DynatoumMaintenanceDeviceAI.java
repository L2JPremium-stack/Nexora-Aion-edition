package ai.instance.illuminaryObelisk;

import java.util.concurrent.Future;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.SkillTemplate;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author M.O.G. Dision, Estrayl
 */
@AIName("dynatoum_maintenance_device")
public class DynatoumMaintenanceDeviceAI extends NpcAI {

	private Future<?> skillTask;

	public DynatoumMaintenanceDeviceAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		Npc dynatoum = getPosition().getWorldMapInstance().getNpc(getPosition().getMapId() == 301230000 ? 233740 : 234686);
		AIActions.targetCreature(this, dynatoum);
		scheduleSkillTask();
	}

	private void scheduleSkillTask() {
		skillTask = ThreadPoolManager.getInstance().schedule(() -> {
			if (!isDead())
				AIActions.useSkill(this, 21535);
		}, 10000);
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		if (skillTemplate.getSkillId() == 21535)
			scheduleSkillTask();
	}

	@Override
	protected void handleDespawned() {
		skillTask.cancel(true);
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
