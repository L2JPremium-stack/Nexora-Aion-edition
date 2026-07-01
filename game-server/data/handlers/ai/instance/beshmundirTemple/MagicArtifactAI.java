package ai.instance.beshmundirTemple;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI;

/**
 * @author Luzien
 */
@AIName("magicartifact")
public class MagicArtifactAI extends AggressiveNpcAI {

	private boolean cooldown = false;

	public MagicArtifactAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (!cooldown) {
			AIActions.useSkill(this, 18916);
			setCD();
		}
	}

	private void setCD() { // ugly hack to prevent overflow TODO: remove on AI improve
		cooldown = true;

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				cooldown = false;
			}
		}, 1000);
	}
}
