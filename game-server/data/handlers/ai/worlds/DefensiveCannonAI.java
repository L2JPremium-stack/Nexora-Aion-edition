package ai.worlds;

import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.skillengine.SkillEngine;

import ai.ActionItemNpcAI;

/**
 * @author xTz, Neon
 */
@AIName("defensive_cannon")
public class DefensiveCannonAI extends ActionItemNpcAI {

	private final AtomicBoolean canUse = new AtomicBoolean(true);

	public DefensiveCannonAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		if (canUse.compareAndSet(true, false)) {
			switch (getNpcId()) {
				case 831338:
				case 831339:
					SkillEngine.getInstance().getSkill(getOwner(), 20364, 60, player).useNoAnimationSkill(); // Board Artillery Morph
			}
			AIActions.deleteOwner(this);
		}
	}

	@Override
	public boolean ask(AIQuestion question) {
		return switch (question) {
			case REWARD_AP_XP_DP_LOOT -> false;
			default -> super.ask(question);
		};
	}
}
