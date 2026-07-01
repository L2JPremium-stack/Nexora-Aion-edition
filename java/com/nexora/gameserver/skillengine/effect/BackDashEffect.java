package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.geoEngine.math.Vector3f;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.skillengine.model.DashStatus;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.world.World;
import com.nexora.gameserver.world.geo.GeoService;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BackDashEffect")
public class BackDashEffect extends DamageEffect {

	@XmlAttribute(name = "distance")
	private float distance;

	@Override
	public void calculate(Effect effect) {
		effect.setDashStatus(DashStatus.BACKDASH);
		Creature effector = effect.getEffector();
		byte h = PositionUtil.getHeadingTowards(effector, effect.getEffected());
		float inverseAngle = PositionUtil.convertHeadingToAngle(h) + 180; // flip by 180 degrees for opposite direction
		Vector3f closestCollision = GeoService.getInstance().findMovementCollision(effector, inverseAngle, distance);
		effect.getSkill().setTargetPosition(closestCollision.getX(), closestCollision.getY(), closestCollision.getZ(), h);
		World.getInstance().updatePosition(effector, closestCollision.getX(), closestCollision.getY(), closestCollision.getZ(), h);
		super.calculate(effect);
	}
}
