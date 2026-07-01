package ai.instance.nightmareCircus;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.controllers.attack.AggroTarget;
import com.nexora.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI;

/**
 * @author Ritsu
 */
@AIName("harlequinlordreshkasummon")
public class HarlequinLordReshkaSummonAI extends AggressiveNpcAI {

	public HarlequinLordReshkaSummonAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		Npc boss = getPosition().getWorldMapInstance().getNpc(233453);
		if (boss != null && !boss.isDead())
			getAggroList().addHate(boss.getAggroList().getTarget(AggroTarget.MOST_HATED), 1);
		super.handleSpawned();
	}
}
