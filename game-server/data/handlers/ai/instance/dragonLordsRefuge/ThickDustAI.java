package ai.instance.dragonLordsRefuge;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Estrayl March 10th, 2018
 */
@AIName("thick_dust")
public class ThickDustAI extends NpcAI {

	public ThickDustAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(() -> AIActions.deleteOwner(this), 10000);
	}
}
