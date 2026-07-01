package ai.instance.darkPoeta;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.model.SkillTemplate;

import ai.AggressiveNpcAI;

/**
 * @author Estrayl
 */
@AIName("tahabata_gargoyle")
public class TahabataGargoyleAI extends AggressiveNpcAI {

	public TahabataGargoyleAI(Npc owner) {
		super(owner);
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		if (skillTemplate.getSkillId() == 18219)
			getOwner().getController().delete();
	}
}
