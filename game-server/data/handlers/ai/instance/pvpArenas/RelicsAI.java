package ai.instance.pvpArenas;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.instance.instancescore.InstanceScore;

import ai.ActionItemNpcAI;

/**
 * @author xTz
 */
@AIName("pvparenarelics")
public class RelicsAI extends ActionItemNpcAI {

	private boolean isRewarded;

	public RelicsAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		InstanceScore<?> instance = getPosition().getWorldMapInstance().getInstanceHandler().getInstanceScore();
		if (instance != null && !instance.isStartProgress()) {
			return;
		}
		super.handleDialogStart(player);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		if (!isRewarded) {
			isRewarded = true;
			AIActions.handleUseItemFinish(this, player);
			final int npcId = getNpcId();
			if (npcId != 701187 && npcId != 701188) {
				AIActions.scheduleRespawn(this);
			}
			AIActions.deleteOwner(this);
		}
	}
}
