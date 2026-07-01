package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.geoEngine.math.Vector3f;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.SkillAliasLocation;
import com.nexora.gameserver.skillengine.model.SkillAliasPosition;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.world.geo.GeoService;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetTeleportEffect")
public class TargetTeleportEffect extends EffectTemplate {

	@XmlAttribute(name = "alias_location")
	protected String loc;

	@XmlAttribute
	protected int distance; // TODO: find out what this value does. Its not the distance.

	@Override
	public void applyEffect(Effect effect) {
		if (effect.getEffected() instanceof Player p) {
			if (loc == null) { // teleport in front of effector
				final Creature effector = effect.isReflected() ? effect.getOriginalEffected() : effect.getEffector();
				double radian = Math.toRadians(PositionUtil.convertHeadingToAngle(effector.getHeading()));
				float z = effector.getZ();
				final float x1 = (float) Math.cos(radian);
				final float y1 = (float) Math.sin(radian);
				Vector3f closestCollision = GeoService.getInstance().getClosestCollision(effect.getEffected(),effector.getX() + x1, effector.getY() + y1, z);
				TeleportService.teleportTo(p, p.getWorldId(), closestCollision.getX(), closestCollision.getY(), closestCollision.getZ());
			} else { // teleport to random specified position
				SkillAliasLocation skillAliasLocation = DataManager.SKILL_ALIAS_LOCATION_DATA.getSkillAliasLocation(loc);
				if (skillAliasLocation != null && p.getWorldId() == skillAliasLocation.getWorldId()) {
					SkillAliasPosition position = Rnd.get(skillAliasLocation.getSkillAliasPositionList());
					TeleportService.teleportTo(p, p.getWorldId(), position.getX(), position.getY(), position.getZ());
				}
			}
		}
	}
}
