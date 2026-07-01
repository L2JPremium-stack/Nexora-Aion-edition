package ai.worlds.inggison;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.templates.ai.Percentage;

import ai.SummonerAI;

/**
 * @author Luzien, xTz
 */
@AIName("omega")
public class OmegaAI extends SummonerAI {

	public OmegaAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleBeforeSpawn(Percentage percent) {
		AIActions.useSkill(this, 19189);
		AIActions.useSkill(this, 19191);
	}

	@Override
	protected boolean checkBeforeSpawn() {
		return getKnownList().streamPlayers().anyMatch(player -> isInRange(player, 30));
	}
}
