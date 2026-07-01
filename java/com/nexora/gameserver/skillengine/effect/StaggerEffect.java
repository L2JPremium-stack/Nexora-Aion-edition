package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.geoEngine.math.Vector3f;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.SubEffectType;
import com.nexora.gameserver.skillengine.model.SpellStatus;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.world.World;
import com.nexora.gameserver.world.geo.GeoService;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StaggerEffect")
public class StaggerEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(Effect effect) {
		final Creature effected = effect.getEffected();
		effected.getController().cancelCurrentSkill(effect.getEffector());
		effected.getEffectController().removeParalyzeEffects();
		if (effected instanceof Player player) {
			player.getFlyController().onStopGliding();
			player.getController().onStopMove();
		}
		World.getInstance().updatePosition(effected, effect.getTargetX(), effect.getTargetY(), effect.getTargetZ(), effected.getHeading());
		if (effected instanceof Player)
			PacketSendUtility.broadcastPacketAndReceive(effected, new SM_FORCED_MOVE(effect.getEffector(), effected.getObjectId(),
					effect.getTargetX(), effect.getTargetY(), effect.getTargetZ()));
		effect.getEffected().getEffectController().setAbnormal(AbnormalState.STAGGER);
		effect.setAbnormal(AbnormalState.STAGGER);
	}

	@Override
	public void calculate(Effect effect) {
		if (effect.getEffected().getEffectController().isAbnormalSet(AbnormalState.PULLED)
			|| effect.getEffected().getEffectController().isAbnormalSet(AbnormalState.STAGGER)
			|| effect.getEffected().getEffectController().isAbnormalSet(AbnormalState.OPENAERIAL)
			|| effect.getEffected().getEffectController().isAbnormalSet(AbnormalState.STUMBLE))
			return;

		if (!super.calculate(effect, StatEnum.STAGGER_RESISTANCE, SpellStatus.STAGGER))
			return;
		if (effect.isSubEffect() && !(effect.getEffected() instanceof Player))
			effect.setSubEffectType(SubEffectType.STAGGER);
		final Creature effector = effect.getEffector();
		final Creature effected = effect.getEffected();
		// Move effected 2 meters backward as on retail
		double radian = Math.toRadians(PositionUtil.convertHeadingToAngle(PositionUtil.getHeadingTowards(effector, effect.getEffected())));
		float x1 = (float) (Math.cos(radian) * 2);
		float y1 = (float) (Math.sin(radian) * 2);
		Vector3f closestCollision = GeoService.getInstance().getClosestCollision(effected, effected.getX() + x1, effected.getY() + y1, effected.getZ());
		effect.setTargetLoc(closestCollision.getX(), closestCollision.getY(), closestCollision.getZ());
	}

	@Override
	public void endEffect(Effect effect) {
		effect.getEffected().getEffectController().unsetAbnormal(AbnormalState.STAGGER);
	}
}
