package ai.instance.sauroBase;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.skillengine.model.SkillTemplate;

import ai.NoActionAI;

@AIName("guard_captain_ahuradim_generator")
public class GuardCaptainAhuradimGenerator extends NoActionAI {

	public GuardCaptainAhuradimGenerator(Npc owner) {
		super(owner);
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		if (skillTemplate.getSkillId() == 21200) {
			VisibleObject guardCaptainAhuradim = getKnownList().findObject(o -> o.get() instanceof Npc npc && npc.getNpcId() == 230857);
			if (guardCaptainAhuradim != null)
				SkillEngine.getInstance().getSkill(getOwner(),21191, 1, guardCaptainAhuradim).useSkill();
		}
	}
}
