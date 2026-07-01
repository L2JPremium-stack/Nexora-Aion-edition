package com.nexora.gameserver.skillengine.effect;

import java.util.concurrent.ScheduledFuture;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.ai.event.AIEventType;
import com.nexora.gameserver.ai.manager.EmoteManager;
import com.nexora.gameserver.configs.main.GeoDataConfig;
import com.nexora.gameserver.geoEngine.math.Vector3f;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.network.aion.serverpackets.SM_POSITION;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.world.geo.GeoService;

/**
 * @author Yeats
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConfuseEffect")
public class ConfuseEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		Creature effected = effect.getEffected();
		effected.getEffectController().removeHideEffects();

		if (effected instanceof Player && ((Player) effected).isInGlidingState()) {
			((Player) effected).getFlyController().onStopGliding();
		}
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, StatEnum.CONFUSE_RESISTANCE, null);
	}

	@Override
	public void startEffect(Effect effect) {
		final Creature effector = effect.isReflected() ? effect.getOriginalEffected() : effect.getEffector();
		final Creature effected = effect.getEffected();
		effected.getController().cancelCurrentSkill(effect.getEffector());
		effected.getEffectController().setAbnormal(AbnormalState.CONFUSE);
		effect.setAbnormal(AbnormalState.CONFUSE);

		effected.getMoveController().abortMove();
		if (effected instanceof Npc) {
			EmoteManager.emoteStartAttacking((Npc) effected, effector);
			effected.getAi().setStateIfNot(AIState.CONFUSE);
		} else if (effected instanceof Player && effected.isInState(CreatureState.WALK_MODE)) {
			effected.unsetState(CreatureState.WALK_MODE);
			PacketSendUtility.broadcastPacket((Player) effected, new SM_EMOTION(effected, EmotionType.RUN), true);
		}
		if (GeoDataConfig.FEAR_ENABLE) {
			ScheduledFuture<?> confuseTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new ConfuseTask(effected), 0, 1000);
			effect.setPeriodicTask(confuseTask, position);
		}
	}

	@Override
	public void endEffect(Effect effect) {
		effect.getEffected().getEffectController().unsetAbnormal(AbnormalState.CONFUSE);
		effect.getEffected().getMoveController().abortMove();
		PacketSendUtility.broadcastPacketAndReceive(effect.getEffected(), new SM_POSITION(effect.getEffected()));

		if (effect.getEffected() instanceof Npc) {
			effect.getEffected().getAi().setStateIfNot(AIState.IDLE);
			effect.getEffected().getAi().onCreatureEvent(AIEventType.ATTACK, effect.getEffected());
		}
	}

	class ConfuseTask implements Runnable {
		private Creature effected;

		ConfuseTask(Creature effected) {
			this.effected = effected;
		}

		@Override
		public void run() {
			if (effected.getEffectController().isConfused()) {
				float angle = Rnd.nextFloat(360f);
				float maxDistance = effected.getGameStats().getMovementSpeedFloat();
				Vector3f closestCollision = GeoService.getInstance().findMovementCollision(effected, angle, maxDistance);
				if (effected instanceof Npc) {
					((Npc) effected).getMoveController().resetMove();
					((Npc) effected).getMoveController().moveToPoint(closestCollision.getX(), closestCollision.getY(), closestCollision.getZ());
				} else {
					byte heading = PositionUtil.convertAngleToHeading(angle);
					effected.getMoveController().setNewDirection(closestCollision.getX(), closestCollision.getY(), closestCollision.getZ(), heading);
					effected.getMoveController().startMovingToDestination();
				}
			}
		}
	}

}
