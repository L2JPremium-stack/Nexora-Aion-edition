package com.nexora.gameserver.model.templates.spawns.vortexspawns;

import java.util.List;

import javax.xml.bind.annotation.*;

import com.nexora.gameserver.model.templates.spawns.Spawn;
import com.nexora.gameserver.model.vortex.VortexStateType;

/**
 * @author Source
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VortexSpawn")
public class VortexSpawn {

	@XmlAttribute(name = "id")
	private int id;

	public int getId() {
		return id;
	}

	@XmlElement(name = "state_type")
	private List<VortexStateTemplate> stateTemplates;

	public List<VortexStateTemplate> getStateTemplates() {
		return stateTemplates;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "VortexStateTemplate")
	public static class VortexStateTemplate {

		@XmlElement(name = "spawn")
		private List<Spawn> spawns;
		@XmlAttribute(name = "state")
		private VortexStateType stateType;

		public List<Spawn> getSpawns() {
			return spawns;
		}

		public VortexStateType getStateType() {
			return stateType;
		}

	}

}
