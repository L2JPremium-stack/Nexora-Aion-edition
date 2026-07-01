package ai.instance.drakenspire;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;

import ai.AggressiveNpcAI;

/**
 * @author Estrayl
 */
@AIName("wave_entry_sensor")
public class WaveEntrySensorAI extends AggressiveNpcAI {

	public WaveEntrySensorAI(Npc owner) {
		super(owner);
	}

	@Override
	public void handleCreatureDetected(Creature creature) {
		super.handleCreatureDetected(creature);
		if (creature instanceof Player)
			getOwner().getController().delete();
	}
}
