package ai;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 */
@AIName("skillarea")
public class SkillAreaNpcAI extends NpcAI {

	public SkillAreaNpcAI(Npc owner) {
		super(owner);
	}
}
