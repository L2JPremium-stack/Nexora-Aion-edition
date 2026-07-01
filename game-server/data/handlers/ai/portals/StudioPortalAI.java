package ai.portals;

import static com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE.STR_HOUSING_ENTER_NEED_HOUSE;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.house.House;
import com.nexora.gameserver.services.HousingService;
import com.nexora.gameserver.services.instance.InstanceService;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.World;
import com.nexora.gameserver.world.WorldMapInstance;
import com.nexora.gameserver.world.WorldMapType;

import ai.ActionItemNpcAI;

/**
 * @author Rolandas
 */
@AIName("studioportal")
public class StudioPortalAI extends ActionItemNpcAI {

	public StudioPortalAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		return true;
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		WorldMapInstance instance;
		float x, y, z;
		byte heading = 0;
		WorldMapType mapType = WorldMapType.getWorld(player.getWorldId());
		if (mapType == WorldMapType.HOUSING_IDLF_PERSONAL || mapType == WorldMapType.HOUSING_IDDF_PERSONAL) { // leaving studio
			House studio = HousingService.getInstance().getPlayerStudio(player.getPosition().getWorldMapInstance().getOwnerId());
			if (studio == null) // should not happen unless this instance was custom spawned by admin
				return;
			instance = World.getInstance().getWorldMap(studio.getAddress().getExitMapId()).getMainWorldMapInstance();
			x = studio.getAddress().getExitX();
			y = studio.getAddress().getExitY();
			z = studio.getAddress().getExitZ();
		} else { // entering own studio
			House studio = HousingService.getInstance().getPlayerStudio(player.getObjectId());
			if (studio == null) { // doesn't own studio
				PacketSendUtility.sendPacket(player, STR_HOUSING_ENTER_NEED_HOUSE());
				return;
			}
			instance = InstanceService.getOrCreateHouseInstance(studio);
			x = studio.getAddress().getX();
			y = studio.getAddress().getY();
			z = studio.getAddress().getZ();
			heading = studio.getTeleportHeading();
		}
		TeleportService.teleportTo(player, instance, x, y, z, heading, TeleportAnimation.FADE_OUT_BEAM);
	}
}
