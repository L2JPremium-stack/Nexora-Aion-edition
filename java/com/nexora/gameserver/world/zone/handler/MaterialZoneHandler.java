package com.nexora.gameserver.world.zone.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nexora.gameserver.configs.main.GeoDataConfig;
import com.nexora.gameserver.controllers.observer.AbstractCollisionObserver.CheckType;
import com.nexora.gameserver.controllers.observer.AbstractMaterialSkillActor;
import com.nexora.gameserver.controllers.observer.ZoneCollisionMaterialActor;
import com.nexora.gameserver.geoEngine.scene.Spatial;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.materials.MaterialSkill;
import com.nexora.gameserver.model.templates.materials.MaterialTemplate;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.zone.ZoneInstance;

/**
 * @author Rolandas
 */
public class MaterialZoneHandler implements ZoneHandler {

	private final Map<Integer, AbstractMaterialSkillActor> observed = new ConcurrentHashMap<>();
	private final Spatial geometry;
	private final MaterialTemplate template;
	private Race ownerRace = Race.NONE;

	public MaterialZoneHandler(Spatial geometry, MaterialTemplate template) {
		this.geometry = geometry;
		this.template = template;
		String name = geometry.getName();
		if (name.startsWith("BU_AB_DARKSP"))
			ownerRace = Race.ASMODIANS;
		else if (name.startsWith("BU_AB_LIGHTSP"))
			ownerRace = Race.ELYOS;
	}

	@Override
	public void onEnterZone(Creature creature, ZoneInstance zone) {
		if (ownerRace == creature.getRace())
			return;
		List<MaterialSkill> matchingSkills = new ArrayList<>();
		for (MaterialSkill skill : template.getSkills()) {
			if (skill.getTarget().matches(creature))
				matchingSkills.add(skill);
		}
		if (matchingSkills.isEmpty())
			return;
		// Teminon/Primum Landing shield 14 & 15, abyss core 16
		CheckType checkType = geometry.getMaterialId() >= 14 && geometry.getMaterialId() <= 16 ? CheckType.PASS : CheckType.TOUCH;
		ZoneCollisionMaterialActor actor = new ZoneCollisionMaterialActor(creature, geometry, matchingSkills, checkType);
		creature.getObserveController().addObserver(actor);
		observed.put(creature.getObjectId(), actor);
		if (GeoDataConfig.GEO_MATERIALS_SHOWDETAILS && creature instanceof Player player && player.isStaff())
			PacketSendUtility.sendMessage(player, "Entered material zone " + geometry.getName());
		actor.moved();
	}

	@Override
	public void onLeaveZone(Creature creature, ZoneInstance zone) {
		AbstractMaterialSkillActor actor = observed.remove(creature.getObjectId());
		if (actor != null) {
			creature.getObserveController().removeObserver(actor);
			actor.abort();
		}
		if (GeoDataConfig.GEO_MATERIALS_SHOWDETAILS && creature instanceof Player player && player.isStaff()) {
			PacketSendUtility.sendMessage(player, "Left material zone " + geometry.getName());
		}
	}
}
