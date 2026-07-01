package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.controllers.NpcController;
import com.nexora.gameserver.controllers.effect.EffectController;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.world.knownlist.PlayerAwareKnownList;

/**
 * @author LokiReborn
 */
public class GroupGate extends SummonedObject<Creature> {

	public GroupGate(NpcController controller, SpawnTemplate spawnTemplate, Creature creator) {
		super(controller, spawnTemplate, (byte) 1, creator);
		setKnownlist(new PlayerAwareKnownList(this));
		setEffectController(new EffectController(this));
	}

	/**
	 * @return NpcObjectType.GROUPGATE
	 */
	@Override
	public NpcObjectType getNpcObjectType() {
		return NpcObjectType.GROUPGATE;
	}
}
