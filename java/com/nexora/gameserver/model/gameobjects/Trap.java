package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.controllers.NpcController;
import com.nexora.gameserver.controllers.effect.EffectController;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.stats.container.NpcLifeStats;
import com.nexora.gameserver.model.stats.container.TrapGameStats;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.world.knownlist.NpcKnownList;

/**
 * @author ATracer
 */
public class Trap extends SummonedObject<Creature> {

	public Trap(NpcController controller, SpawnTemplate spawnTemplate, Creature creator) {
		super(controller, spawnTemplate, DataManager.NPC_DATA.getNpcTemplate(spawnTemplate.getNpcId()).getLevel(), creator);
		setMasterName("");
		setKnownlist(new NpcKnownList(this));
		setEffectController(new EffectController(this));
	}

	@Override
	protected void setupStatContainers() {
		setGameStats(new TrapGameStats(this));
		setLifeStats(new NpcLifeStats(this));
	}

	@Override
	public byte getLevel() {
		return getCreator() == null ? 1 : getCreator().getLevel();
	}

	/**
	 * @return NpcObjectType.TRAP
	 */
	@Override
	public NpcObjectType getNpcObjectType() {
		return NpcObjectType.TRAP;
	}
}
