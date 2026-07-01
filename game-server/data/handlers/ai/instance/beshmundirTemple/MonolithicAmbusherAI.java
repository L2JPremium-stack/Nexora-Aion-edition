package ai.instance.beshmundirTemple;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.event.AIEventType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI;

/**
 * @author xTz
 */
@AIName("monolithicambusher")
public class MonolithicAmbusherAI extends AggressiveNpcAI {

	private boolean hasHelped;

	public MonolithicAmbusherAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		hasHelped = false;
	}

	@Override
	protected void handleCreatureAggro(Creature creature) {
		super.handleCreatureAggro(creature);
		if (!hasHelped) {
			hasHelped = true;
			help(creature);
		}
	}

	private void help(Creature creature) {
		getKnownList().forEachNpc(npc -> {
			if (isInRange(npc, 60)) {
				if (!npc.isDead() && npc.getNpcId() == 216215 && (int) npc.getSpawn().getY() == (int) getSpawnTemplate().getY()) {
					npc.getAi().onCreatureEvent(AIEventType.CREATURE_AGGRO, creature);
				}
			}
		});
	}
}
