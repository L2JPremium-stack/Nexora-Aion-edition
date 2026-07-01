package ai.instance.rentusBase;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.SkillEngine;

/**
 * @author xTz
 */
@AIName("collapsed_reian_building")
public class CollapsedReianBuildingAI extends NpcAI {

	public CollapsedReianBuildingAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		SkillEngine.getInstance().getSkill(getOwner(), 20088, 60, getOwner()).useNoAnimationSkill();
	}

}
