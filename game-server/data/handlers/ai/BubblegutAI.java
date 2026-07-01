package ai;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;

/**
 * @author Luzien
 */
@AIName("bubblegut")
public class BubblegutAI extends GeneralNpcAI {

	public BubblegutAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		AIActions.useSkill(this, 16447);
	}

}
