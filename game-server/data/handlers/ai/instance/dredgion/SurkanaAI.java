package ai.instance.dredgion;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.event.AIEventType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;

import ai.OneDmgNoActionAI;

/**
 * recieve only 1 dmg with each attack(handled by super) Aggro the whole room on attack
 * 
 * @author Luzien
 */
@AIName("surkana")
public class SurkanaAI extends OneDmgNoActionAI {

	public SurkanaAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		// roomaggro
		checkForSupport(creature);
	}

	private void checkForSupport(Creature creature) {
		getKnownList().forEachNpc(npc -> {
			if (!npc.isDead() && isInRange(npc, 25))
				npc.getAi().onCreatureEvent(AIEventType.CREATURE_AGGRO, creature);
		});
	}
}
