package com.nexora.gameserver.network.aion.clientpackets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Friend;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.house.House;
import com.nexora.gameserver.model.team.legion.Legion;
import com.nexora.gameserver.model.templates.npc.NpcTemplateType;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.HousingService;
import com.nexora.gameserver.services.instance.InstanceService;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.utils.audit.AuditLogger;
import com.nexora.gameserver.world.WorldMapInstance;

/**
 * Packet for teleporting by using relationship crystal
 *
 * @author Rolandas
 */
public class CM_HOUSE_TELEPORT extends AionClientPacket {

	private int actionId;
	private int playerId1;
	private int playerId2;

	public CM_HOUSE_TELEPORT(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		actionId = readUC();
		playerId1 = readD(); // just why? without this field we wouldn't even have to check exploitations
		playerId2 = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null)
			return;
		if (playerId1 != player.getObjectId()) {
			AuditLogger.log(player, "tried to teleport playerId " + playerId1 + " instead of himself");
			return;
		}

		VisibleObject target = player.getTarget();
		if (!(target instanceof Npc))
			return;
		Npc relationshipCrystal = (Npc) target;
		if (relationshipCrystal.getNpcTemplateType() != NpcTemplateType.HOUSING || !relationshipCrystal.getAi().getName().equals("friendportal")) {
			AuditLogger.log(player, "tried to use house teleport without targeting a relationship crystal: " + target);
			return;
		}

		House house;
		switch (actionId) {
			case 1: // to own house
				house = player.getActiveHouse();
				break;
			case 2: // to friends house
				if (playerId2 == 0)
					return;
				List<House> friendsAccessibleHouses = findFriendsAccessibleHouses(player);
				house = friendsAccessibleHouses.stream().filter(h -> h.getOwnerId() == playerId2).findAny().orElse(null);
				break;
			case 3: // to random friend's house
				house = Rnd.get(findFriendsAccessibleHouses(player));
				if (house == null) {
					sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_NO_RELATIONSHIP_RECENTLY());
					return;
				}
				break;
			default:
				LoggerFactory.getLogger(getClass()).warn("Unhandled house teleport actionId " + actionId);
				return;
		}

		if (house == null)
			return;

		WorldMapInstance instance = InstanceService.getOrCreateHouseInstance(house);
		TeleportService.teleportTo(player, instance, house.getX(), house.getY(), house.getZ(), house.getTeleportHeading(),
			TeleportAnimation.FADE_OUT_BEAM);
	}

	private List<House> findFriendsAccessibleHouses(Player player) {
		List<House> houses = new ArrayList<>();
		for (Friend friend : player.getFriendList())
			addHouseIfAccessible(player, houses, friend.getObjectId());
		Legion legion = player.getLegion();
		if (legion != null) {
			for (int memberId : legion.getMemberIds()) {
				if (memberId != player.getObjectId())
					addHouseIfAccessible(player, houses, memberId);
			}
		}
		return houses;
	}

	private void addHouseIfAccessible(Player player, List<House> relationIds, int friendId) {
		House house = HousingService.getInstance().findActiveHouse(friendId);
		if (house != null && house.canEnter(player))
			relationIds.add(house);
	}

}
