package ai.instance.sauroBase;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.SkillTemplate;

import ai.AggressiveNpcAI;

/**
 * @author Estrayl
 */
@AIName("sauro_drone")
public class SauroDroneAI extends AggressiveNpcAI {

	public SauroDroneAI(Npc owner) {
		super(owner);
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		if (skillTemplate.getSkillId() == 19498)
			getOwner().getController().delete();
	}
}
