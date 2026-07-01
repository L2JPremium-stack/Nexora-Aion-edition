package ai.instance.drakenspire;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI;

/**
 * @author Estrayl
 */
@AIName("wave_event_bomber")
public class WaveEventBomberAI extends GeneralNpcAI {

	public WaveEventBomberAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		PacketSendUtility.broadcastMessage(getOwner(), 1501312, 4000);
	}
}
