package ai.instance.nightmareCircus;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.templates.ai.SummonGroup;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.SummonerAI;

/**
 * @author Farlon
 */
@AIName("mistressviloa")
public class MistressViloaAI extends SummonerAI {

	public MistressViloaAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		PacketSendUtility.broadcastMessage(getOwner(), 1501135, 3000);
	}

	@Override
	protected void handleSpawnFinished(SummonGroup summonGroup) {
		PacketSendUtility.broadcastMessage(getOwner(), 1501134, 1000);
	}
}
