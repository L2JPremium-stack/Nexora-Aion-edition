package com.nexora.gameserver.skillengine.effect;

import java.util.concurrent.Future;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.GroupGate;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.spawnengine.SpawnEngine;
import com.nexora.gameserver.spawnengine.VisibleObjectSpawner;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author LokiReborn, Neon
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonGroupGateEffect")
public class SummonGroupGateEffect extends SummonEffect {

	@Override
	public void applyEffect(Effect effect) {

		Creature effector = effect.getEffector();
		float x = effect.getX();
		float y = effect.getY();
		float z = effect.getZ();
		byte heading = effector.getHeading();
		int worldId = effector.getWorldId();
		int instanceId = effector.getInstanceId();

		SpawnTemplate spawn = SpawnEngine.newSingleTimeSpawn(worldId, npcId, x, y, z, heading);
		final GroupGate groupgate = VisibleObjectSpawner.spawnGroupGate(spawn, instanceId, effector);

		Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				groupgate.getController().delete();
			}
		}, time * 1000);
		groupgate.getController().addTask(TaskId.DESPAWN, task);
	}
}
