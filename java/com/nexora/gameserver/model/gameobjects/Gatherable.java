package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.controllers.GatherableController;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.templates.gather.GatherableTemplate;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.utils.idfactory.IDFactory;
import com.nexora.gameserver.world.WorldPosition;
import com.nexora.gameserver.world.knownlist.PlayerAwareKnownList;

/**
 * @author ATracer
 */
public class Gatherable extends VisibleObject {

	public Gatherable(SpawnTemplate spawnTemplate, GatherableController controller) {
		super(IDFactory.getInstance().nextId(), controller, spawnTemplate, DataManager.GATHERABLE_DATA.getGatherableTemplate(spawnTemplate.getNpcId()), new WorldPosition(spawnTemplate.getWorldId()), true);
		controller.setOwner(this);
		setKnownlist(new PlayerAwareKnownList(this));
	}

	@Override
	public GatherableTemplate getObjectTemplate() {
		return (GatherableTemplate) super.getObjectTemplate();
	}

	@Override
	public GatherableController getController() {
		return (GatherableController) super.getController();
	}
}
