package com.nexora.gameserver.model.templates.spawns.panesterra;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.templates.spawns.Spawn;
import com.nexora.gameserver.services.panesterra.ahserion.PanesterraFaction;

/**
 * @author Yeats
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AhserionsFlightSpawn")
public class AhserionsFlightSpawn {
	
	@XmlElement(name = "ahserion_stage_spawn")
	private List<AhserionStageSpawnTemplate> ahserionStageSpawnTemplate;
	@XmlAttribute(name = "faction")
	private PanesterraFaction faction;
	
	public PanesterraFaction getFaction() {
		return faction;
	}
	
	public List<AhserionStageSpawnTemplate> getStageSpawnTemplate() {
		return ahserionStageSpawnTemplate;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "AhserionStageSpawnTemplate")
	public static class AhserionStageSpawnTemplate {
		
		@XmlElement(name = "spawn")
		private List<Spawn> spawns;
		@XmlAttribute(name = "stage")
		private int stage = 0;
		
		public int getStage() {
			return stage;
		}
		
		public List<Spawn> getSpawns() {
			return spawns;
		}
	}
}
