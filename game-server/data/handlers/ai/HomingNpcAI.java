package ai;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.AttackIntention;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 */
@AIName("homing")
public class HomingNpcAI extends GeneralNpcAI {

	public HomingNpcAI(Npc owner) {
		super(owner);
	}

	@Override
	public void think() {
		// homings are not thinking to return :)
	}

	@Override
	public AttackIntention chooseAttackIntention() {
		if (getTarget() != null && chooseSkillAttack(false))
			return AttackIntention.SKILL_ATTACK;

		return AttackIntention.SIMPLE_ATTACK;
	}

	@Override
	public boolean ask(AIQuestion question) {
		return switch (question) {
			case ALLOW_DECAY, ALLOW_RESPAWN, REWARD_AP_XP_DP_LOOT -> false;
			default -> super.ask(question);
		};
	}

}
