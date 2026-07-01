package ai.instance.theHexway;

import java.util.ArrayList;
import java.util.List;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.ChatType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.world.geo.GeoService;

import ai.ActionItemNpcAI;

/**
 * @author Sykra
 */
@AIName("chest_teleporter")
public class ChestTeleporterAI extends ActionItemNpcAI {

	private final List<Integer> recievedShouts = new ArrayList<>();

	public ChestTeleporterAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		if (player.getWorldId() == 300700000)
			TeleportService.teleportTo(player, 300700000, 485.59f, 585.42f, 357f, (byte) 60);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (!(creature instanceof Player) || creature.isDead() || !getOwner().canSee(creature) || !getOwner().getPosition().isMapRegionActive())
			return;
		synchronized (recievedShouts) {
			if (!recievedShouts.contains(creature.getObjectId())) {
				if (PositionUtil.isInRange(getOwner(), creature, 8) && GeoService.getInstance().canSee(getOwner(), creature)) {
					recievedShouts.add(creature.getObjectId());
					PacketSendUtility.sendPacket((Player) creature,
						new SM_MESSAGE(getOwner(), "A mysterious orb of relocation is hidden in this chest", ChatType.NPC));
				}
			}
		}
	}

}
