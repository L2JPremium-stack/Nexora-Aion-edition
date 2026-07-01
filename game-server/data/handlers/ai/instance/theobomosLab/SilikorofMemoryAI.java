package ai.instance.theobomosLab;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.HpPhases;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI;

/**
 * @author Ritsu
 */
@AIName("silikor")
public class SilikorofMemoryAI extends AggressiveNpcAI implements HpPhases.PhaseHandler {

	private final HpPhases hpPhases = new HpPhases(50, 25, 10);

	public SilikorofMemoryAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		hpPhases.tryEnterNextPhase(this);
	}

	@Override
	public void handleHpPhase(int phaseHpPercent) {
		sp(281054);
		sp(281053);
	}

	private void sp(int npcId) {
		double angleRadians = Math.toRadians(Rnd.nextFloat(360f));
		int distance = Rnd.get(0, 2);
		float x1 = (float) (Math.cos(angleRadians) * distance);
		float y1 = (float) (Math.sin(angleRadians) * distance);
		WorldPosition p = getPosition();
		spawn(npcId, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		if (getNpcId() == 214668)
			SkillEngine.getInstance().getSkill(getOwner(), 18481, 1, getOwner()).useSkill();
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		hpPhases.reset();
	}

	@Override
	protected void handleDied() {
		getPosition().getWorldMapInstance().getNpcs(281054, 281053).forEach(npc -> npc.getController().delete());
		super.handleDied();
	}

}
