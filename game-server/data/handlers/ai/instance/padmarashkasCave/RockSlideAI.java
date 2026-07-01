package ai.instance.padmarashkasCave;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.SkillTemplate;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI;

/**
 * @author Ritsu, Estrayl
 */
@AIName("rock_slide")
public class RockSlideAI extends AggressiveNpcAI {

	public RockSlideAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(this::useRockSlide, 2000);
	}

	@Override
	public float modifyDamage(Creature attacker, float damage, Effect effect) {
		return 0;
	}

	private void useRockSlide() {
		AIActions.targetSelf(this);
		AIActions.useSkill(this, 19295);
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		ThreadPoolManager.getInstance().schedule(this::useRockSlide, 2700);
	}
}
