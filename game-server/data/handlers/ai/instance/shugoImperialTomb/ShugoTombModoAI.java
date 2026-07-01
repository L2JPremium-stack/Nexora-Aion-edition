package ai.instance.shugoImperialTomb;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;

/**
 * @author Ritsu
 */
@AIName("shugo_tomb_modo")
public class ShugoTombModoAI extends ShugoTombAttackerAI {

	public ShugoTombModoAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		handleHate();
	}
}
