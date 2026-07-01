package com.nexora.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.road.Road;
import com.nexora.gameserver.model.templates.road.RoadTemplate;
import com.nexora.gameserver.world.World;

/**
 * @author SheppeR
 */
public class RoadService {

	Logger log = LoggerFactory.getLogger(RoadService.class);

	private static class SingletonHolder {

		protected static final RoadService instance = new RoadService();
	}

	public static final RoadService getInstance() {
		return SingletonHolder.instance;
	}

	private RoadService() {
		for (RoadTemplate rt : DataManager.ROAD_DATA.getRoadTemplates()) {
			for (Integer instanceId : World.getInstance().getWorldMap(rt.getMap()).getAvailableInstanceIds()) {
				Road r = new Road(rt, instanceId);
				r.spawn();
				log.debug("Added " + r.getName() + " at m=" + r.getWorldId() + ",x=" + r.getX() + ",y=" + r.getY() + ",z=" + r.getZ() + " [" + instanceId
					+ "]");
			}
		}
	}
}
