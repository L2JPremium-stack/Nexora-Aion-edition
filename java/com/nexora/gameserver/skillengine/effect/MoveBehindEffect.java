package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.geoEngine.math.Vector3f;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.skillengine.model.DashStatus;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.world.World;
import com.nexora.gameserver.world.geo.GeoService;

/**
 * @author Sarynth, Bobobear
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MoveBehindEffect")
public class MoveBehindEffect extends DamageEffect {

	@Override
	public void calculate(Effect effect) {
		effect.setDashStatus(DashStatus.MOVEBEHIND);
		final Creature effector = effect.getEffector();
		final Creature effected = effect.getEffected();
		double radian = Math.toRadians(PositionUtil.convertHeadingToAngle(effected.getHeading()));
		float distance = effector.getObjectTemplate().getBoundRadius().getMaxOfFrontAndSide() + effected.getObjectTemplate().getBoundRadius().getMaxOfFrontAndSide() + 1;
		float x1 = (float) Math.cos(Math.PI + radian) * distance;
		float y1 = (float) Math.sin(Math.PI + radian) * distance;
		Vector3f closestCollision = GeoService.getInstance().getClosestCollision(effector, effected.getX() + x1, effected.getY() + y1, effected.getZ());
		byte h = PositionUtil.getHeadingTowards(effector, effected);
		World.getInstance().updatePosition(effector, closestCollision.getX(), closestCollision.getY(), closestCollision.getZ(), h);
		// set target position for SM_CASTSPELL_RESULT
		effect.getSkill().setTargetPosition(closestCollision.getX(), closestCollision.getY(), closestCollision.getZ(), h);
		super.calculate(effect);
	}
}
