package com.nexora.gameserver.model.templates.spawns.panesterra;

import com.nexora.gameserver.model.templates.spawns.SpawnGroup;
import com.nexora.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.services.panesterra.ahserion.PanesterraFaction;

/**
 * 
 * @author Yeats
 *
 */
public class AhserionsFlightSpawnTemplate extends SpawnTemplate {

	private int stage;
	private PanesterraFaction faction;
	
	public AhserionsFlightSpawnTemplate(SpawnGroup spawnGroup, SpawnSpotTemplate spot) {
		super(spawnGroup, spot);
	}
	
	public int getStage() {
		return stage;
	}
	
	public PanesterraFaction getFaction() {
		return faction;
	}
	
	public void setStage(int stage) {
		this.stage = stage;
	}
	
	public void setPanesterraTeam(PanesterraFaction faction) {
		this.faction = faction;
	}
}
