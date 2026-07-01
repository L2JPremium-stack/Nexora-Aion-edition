package com.nexora.gameserver.model.templates.spawns.riftspawns;

import com.nexora.gameserver.model.templates.spawns.SpawnGroup;
import com.nexora.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;

/**
 * @author Source
 */
public class RiftSpawnTemplate extends SpawnTemplate {

	private int id;

	public RiftSpawnTemplate(SpawnGroup spawnGroup, SpawnSpotTemplate spot) {
		super(spawnGroup, spot);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
