package ai.walkers;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.handler.MoveEventHandler;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;

/**
 * @author Rolandas
 */
@AIName("polorserin")
public class PolorSerinAI extends WalkGeneralRunnerAI {

	public PolorSerinAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleMoveArrived() {
		boolean adultsNear = getKnownList().stream()
			.anyMatch(obj -> obj.get() instanceof Npc npc && (npc.getNpcId() == 203129 || npc.getNpcId() == 203132) && isInRange(npc, getOwner().getAggroRange()));
		if (adultsNear) {
			MoveEventHandler.onMoveArrived(this);
			getOwner().unsetState(CreatureState.WEAPON_EQUIPPED);
		} else {
			super.handleMoveArrived();
		}
	}
}
