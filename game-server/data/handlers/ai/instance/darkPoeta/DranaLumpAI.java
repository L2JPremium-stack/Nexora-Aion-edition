package ai.instance.darkPoeta;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.skillengine.model.SkillTemplate;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.ActionItemNpcAI;

/**
 * @author Ritsu, Estrayl
 */
@AIName("drana_lump")
public class DranaLumpAI extends ActionItemNpcAI {

	public DranaLumpAI(Npc owner) {
		super(owner);
	}

	@Override
	public void handleCreatureDetected(Creature creature) {
		if (creature instanceof Npc) {
			switch (((Npc) creature).getNpcId()) {
				case 214880:
				case 215388:
				case 215389:
					checkDistance((Npc) creature);
					break;
			}
		}
	}

	private void checkDistance(Npc npc) {
		ThreadPoolManager.getInstance().schedule(() -> {
			if (PositionUtil.getDistance(getOwner(), npc) <= 2)
				SkillEngine.getInstance().getSkill(getOwner(), 18536, 46, npc).useSkill();
			else
				checkDistance(npc);
		}, 4000);
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		if (skillTemplate.getSkillId() == 18536)
			getOwner().getController().delete();
	}
}
