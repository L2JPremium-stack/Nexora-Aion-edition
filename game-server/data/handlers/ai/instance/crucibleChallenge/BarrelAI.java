package ai.instance.crucibleChallenge;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;

/**
 * @author xTz
 */
@AIName("barrel")
public class BarrelAI extends NpcAI {

	public BarrelAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		switch (getNpcId()) {
			case 218560 -> rndSpawnInRange(218561, 4);
			case 217840 -> rndSpawnInRange(217841, 4);
		}
		AIActions.deleteOwner(this);
	}
}
