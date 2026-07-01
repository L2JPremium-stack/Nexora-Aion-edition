package ai.instance.rakes;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.skillengine.SkillEngine;

import ai.ActionItemNpcAI;

@AIName("big_badaboom")
public class BigBadaboomAI extends ActionItemNpcAI {

	public BigBadaboomAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		int morphSkill = 0;
		switch (getNpcId()) {
			case 231016: // Big Badaboom.
			case 231017: // Bigger Badaboom.
				morphSkill = 0x4E502E;
				break;
		}
		SkillEngine.getInstance().getSkill(getOwner(), morphSkill >> 8, morphSkill & 0xFF, player).useNoAnimationSkill();
		AIActions.deleteOwner(this);
	}
}
