package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.controllers.StaticObjectController;
import com.nexora.gameserver.model.templates.VisibleObjectTemplate;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.utils.idfactory.IDFactory;
import com.nexora.gameserver.world.WorldPosition;

/**
 * @author ATracer
 */
public class StaticObject extends VisibleObject {

	public StaticObject(StaticObjectController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate) {
		super(IDFactory.getInstance().nextId(), controller, spawnTemplate, objectTemplate, new WorldPosition(spawnTemplate.getWorldId()), true);
		controller.setOwner(this);
	}
}
