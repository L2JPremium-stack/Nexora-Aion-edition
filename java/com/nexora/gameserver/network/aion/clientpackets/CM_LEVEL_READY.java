package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.configs.main.EventsConfig;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.animations.ArrivalAnimation;
import com.nexora.gameserver.model.gameobjects.Pet;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.FlyState;
import com.nexora.gameserver.model.items.storage.StorageType;
import com.nexora.gameserver.model.templates.windstreams.Location2D;
import com.nexora.gameserver.model.templates.windstreams.WindstreamTemplate;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.*;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.services.SiegeService;
import com.nexora.gameserver.services.TownService;
import com.nexora.gameserver.services.WeatherService;
import com.nexora.gameserver.services.conquerorAndProtectorSystem.ConquerorAndProtectorService;
import com.nexora.gameserver.services.event.EventService;
import com.nexora.gameserver.services.instance.InstanceService;
import com.nexora.gameserver.services.rift.RiftInformer;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.world.World;

/**
 * Client is saying that level[map] is ready.
 *
 * @author -Nemesiss-, Kwazar
 */
public class CM_LEVEL_READY extends AionClientPacket {

	public CM_LEVEL_READY(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
	}

	@Override
	protected void runImpl() {
		Player activePlayer = getConnection().getActivePlayer();

		if (activePlayer.getActiveHouse() != null)
			sendPacket(new SM_HOUSE_OBJECTS(activePlayer.getActiveHouse().getRegistry().getSpawnedObjects()));
		if (activePlayer.isInInstance()) {
			sendPacket(new SM_INSTANCE_COUNT_INFO(activePlayer.getWorldId(), activePlayer.getInstanceId()));
		}
		sendPacket(new SM_PLAYER_INFO(activePlayer));
		activePlayer.getController().startProtectionActiveTask();
		sendPacket(new SM_ACCOUNT_PROPERTIES());
		sendPacket(new SM_MOTION(activePlayer.getObjectId(), activePlayer.getMotions().getActiveMotions()));

		WindstreamTemplate template = DataManager.WINDSTREAM_DATA.getStreamTemplate(activePlayer.getPosition().getMapId());
		if (template != null)
			for (Location2D location : template.getLocations().getLocation()) {
				sendPacket(new SM_WINDSTREAM_ANNOUNCE(location.getFlyPathType().getId(), template.getMapId(), location.getId(), location.getState()));
			}

		// Spawn player into the world.
		World.getInstance().spawn(activePlayer);

		if (activePlayer.isInFlyState(FlyState.FLYING)) // notify client if we are still flying (client always ends flying after teleport)
			activePlayer.getFlyController().startFly(true, true);

		// SM_SHIELD_EFFECT, SM_ABYSS_ARTIFACT_INFO3
		if (activePlayer.isInSiegeWorld()) {
			SiegeService.getInstance().onEnterSiegeWorld(activePlayer);
		}

		// SM_CONQUEROR_PROTECTOR
		ConquerorAndProtectorService.getInstance().onEnterMap(activePlayer);

		// SM_RIFT_ANNOUNCE
		RiftInformer.sendRiftsInfo(activePlayer);

		// SM_UPGRADE_ARCADE
		if (EventsConfig.ENABLE_EVENT_ARCADE)
			sendPacket(new SM_UPGRADE_ARCADE(true));

		// SM_NEARBY_QUESTS
		activePlayer.getController().updateNearbyQuests();

		// SM_QUEST_REPEAT
		activePlayer.getController().updateRepeatableQuests();

		// Loading weather for the player's region
		WeatherService.getInstance().loadWeather(activePlayer);

		QuestEngine.getInstance().onEnterWorld(activePlayer);

		activePlayer.getController().onEnterWorld();
		InstanceService.onEnterInstance(activePlayer);
		activePlayer.getEffectController().updatePlayerEffectIcons(null);
		sendPacket(SM_CUBE_UPDATE.cubeSize(StorageType.CUBE, activePlayer));

		Pet pet = activePlayer.getPet();
		if (pet != null && !pet.isSpawned())
			World.getInstance().spawn(pet);
		activePlayer.setPortAnimation(ArrivalAnimation.NONE);

		TownService.getInstance().onEnterWorld(activePlayer);
		EventService.getInstance().onEnterMap(activePlayer);

		var team = activePlayer.getCurrentTeam();
		if (team != null)
			ThreadPoolManager.getInstance().schedule(() -> team.sendBrands(activePlayer), 100); // delayed to fix brands when returning from studios/houses
	}
}
