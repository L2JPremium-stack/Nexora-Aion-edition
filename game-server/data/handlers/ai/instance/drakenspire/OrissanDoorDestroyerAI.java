package ai.instance.drakenspire;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.GeneralNpcAI;

/**
 * @author Estrayl
 */
@AIName("orissan_door_destroyer")
public class OrissanDoorDestroyerAI extends GeneralNpcAI {

	public OrissanDoorDestroyerAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		scheduleGateDestruction();
	}

	private void scheduleGateDestruction() {
		PacketSendUtility.broadcastMessage(getOwner(), 1501313, 7000);
		ThreadPoolManager.getInstance().schedule(() -> {
			for (Npc npc : getPosition().getWorldMapInstance().getNpcs(731580))
				if (isInRange(npc, 15))
					SkillEngine.getInstance().getSkill(npc, 20840, 1, npc).useWithoutPropSkill();
		}, 14000);
	}
}
