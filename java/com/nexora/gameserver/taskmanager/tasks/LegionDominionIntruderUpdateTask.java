package com.nexora.gameserver.taskmanager.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nexora.gameserver.configs.main.CustomConfig;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.legionDominion.LegionDominionLocation;
import com.nexora.gameserver.model.templates.cp.CPType;
import com.nexora.gameserver.network.aion.serverpackets.SM_CONQUEROR_PROTECTOR;
import com.nexora.gameserver.services.LegionDominionService;
import com.nexora.gameserver.services.conquerorAndProtectorSystem.CPInfo;
import com.nexora.gameserver.services.conquerorAndProtectorSystem.ConquerorAndProtectorService;
import com.nexora.gameserver.taskmanager.AbstractPeriodicTaskManager;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.World;
import com.nexora.gameserver.world.zone.ZoneName;

public class LegionDominionIntruderUpdateTask extends AbstractPeriodicTaskManager {

	private LegionDominionIntruderUpdateTask() {
		super(4000);
	}

	@Override
	public void run() {
 		if (!CustomConfig.CONQUEROR_AND_PROTECTOR_SYSTEM_ENABLED)
			return;
		Map<Integer, List<LegionDominionLocation>> locationsPerWorldMap = LegionDominionService.getInstance().getLegionDominions().stream()
			.filter(loc -> loc.getLegionId() != 0).collect(Collectors.groupingBy(LegionDominionLocation::getWorldId));
		if (locationsPerWorldMap.isEmpty())
			return;

		for (Map.Entry<Integer, List<LegionDominionLocation>> entry : locationsPerWorldMap.entrySet()) {
			if (entry == null)
				continue;
			List<Player> players = World.getInstance().getWorldMap(entry.getKey()).getMainWorldMapInstance().getPlayersInside();
			for (LegionDominionLocation location : entry.getValue()) {
				if (location.getZoneNameAsString().isEmpty())
					continue;
				ZoneName zoneName = ZoneName.get(location.getZoneNameAsString());
				if (zoneName == ZoneName.NONE)
					continue;
				ConquerorAndProtectorService cpService = ConquerorAndProtectorService.getInstance();
				List<Player> protectors = new ArrayList<>();
				List<Player> conquerors = new ArrayList<>();
				for (Player player : players) {
					if (!player.isInsideZone(zoneName))
						continue;
					CPInfo cpInfo = cpService.getCPInfoForCurrentMap(player);
					if (cpInfo == null || (cpInfo.getType() == CPType.PROTECTOR && cpInfo.getLDRank() != 3))
						continue;
					if (cpInfo.getType() == CPType.CONQUEROR)
						conquerors.add(player);
					else if (cpInfo.getType() == CPType.PROTECTOR)
						protectors.add(player);
				}
				if (!conquerors.isEmpty())
					protectors.forEach(player -> PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(conquerors, false)));
			}
		}
	}

	public static LegionDominionIntruderUpdateTask getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static final class SingletonHolder {

		private static final LegionDominionIntruderUpdateTask INSTANCE = new LegionDominionIntruderUpdateTask();
	}

}
