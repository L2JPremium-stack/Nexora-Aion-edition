package ai.instance.danuarReliquary;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.skillengine.model.SkillTemplate;

/**
 * @author Yeats
 */
@AIName("modors_position_indicator")
public class ModorsPositionIndicatorAI extends NpcAI {

	public ModorsPositionIndicatorAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		SkillEngine.getInstance().getSkill(getOwner(), 21166, 1, getOwner()).useWithoutPropSkill();

	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		if (skillTemplate.getSkillId() == 21166)
			getOwner().getController().delete();
	}
}
