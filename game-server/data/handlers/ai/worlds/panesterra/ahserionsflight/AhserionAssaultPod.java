package ai.worlds.panesterra.ahserionsflight;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.world.WorldPosition;

import ai.GeneralNpcAI;

/**
 * @author Estrayl
 */
@AIName("ahserion_assault_pod")
public class AhserionAssaultPod extends GeneralNpcAI {

	public AhserionAssaultPod(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(this::activate, 400);
	}

	private void activate() {
		WorldPosition p = getPosition();
		SkillEngine.getInstance().getSkill(getOwner(), 20776, 1, getOwner()).useWithoutPropSkill(); // Trooper Shock

		ThreadPoolManager.getInstance().schedule(() -> spawnDefenders(p), 4000);

		ThreadPoolManager.getInstance().schedule(() -> AIActions.deleteOwner(this), 6000);
	}

	private void spawnDefenders(WorldPosition p) {
		spawn(297191, p.getX() + 3, p.getY() - 3, p.getZ() + 3, p.getHeading()); // Ahserion Troopers Assassin
		spawn(297191, p.getX(), p.getY(), p.getZ() + 3, p.getHeading()); // Ahserion Troopers Sorcerer
		spawn(297191, p.getX() + 3, p.getY() + 3, p.getZ() + 3, p.getHeading()); // Ahserion Troopers Assassin
	}
}
