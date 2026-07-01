package ai.walkers;

import java.util.List;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.handler.MoveEventHandler;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.templates.npcshout.NpcShout;
import com.nexora.gameserver.model.templates.npcshout.ShoutEventType;
import com.nexora.gameserver.services.NpcShoutsService;
import com.nexora.gameserver.utils.PositionUtil;

import ai.GeneralNpcAI;

/**
 * @author Rolandas
 */
@AIName("naia")
public class NaiaAI extends GeneralNpcAI {

	boolean saidCannon = false;
	boolean saidQydro = false;

	public NaiaAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleMoveArrived() {
		MoveEventHandler.onMoveArrived(this);

		Npc cannon = getPosition().getWorldMapInstance().getNpc(203145);
		Npc qydro = getPosition().getWorldMapInstance().getNpc(203125);
		boolean isCannonNear = PositionUtil.isInRange(getOwner(), cannon, getOwner().getAggroRange());
		boolean isQydroNear = PositionUtil.isInRange(getOwner(), qydro, getOwner().getAggroRange());

		List<NpcShout> shouts = null;
		if (!saidCannon && isCannonNear) {
			saidCannon = true;
			// TODO: she should get closer and turn to Cannon
			// getOwner().getPosition().setH((byte)60);
			shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(getPosition().getMapId(), getNpcId(), ShoutEventType.WALK_WAYPOINT, "2", 0);
			NpcShoutsService.getInstance().shoutRandom(getOwner(), null, shouts, 10);
		} else if (saidCannon && !isCannonNear) {
			saidCannon = false;
		}
		if (!saidQydro && isQydroNear) {
			saidQydro = true;
			shouts = DataManager.NPC_SHOUT_DATA.getNpcShouts(getPosition().getMapId(), getNpcId(), ShoutEventType.WALK_WAYPOINT, "1", 0);
			NpcShoutsService.getInstance().shoutRandom(getOwner(), null, shouts, 0);
		} else if (saidQydro && !isQydroNear) {
			saidQydro = false;
		}
	}
}
