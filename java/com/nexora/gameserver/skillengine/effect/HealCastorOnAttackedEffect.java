package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.controllers.observer.ActionObserver;
import com.nexora.gameserver.controllers.observer.ObserverType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.nexora.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.HealType;
import com.nexora.gameserver.utils.PositionUtil;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealCastorOnAttackedEffect")
public class HealCastorOnAttackedEffect extends EffectTemplate {

	@XmlAttribute
	protected HealType type;// useless
	@XmlAttribute
	protected float range;

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(final Effect effect) {
		effect.addObserver(effect.getEffected(), new ActionObserver(ObserverType.ATTACKED) {

			@Override
			public void attacked(Creature creature, int skillId) {
				Creature effector = effect.getEffector();
				var group = effector instanceof Player p ? p.getCurrentGroup() : null;
				int healValue = calculateBaseValue(effect);
				if (group == null) {
					if (PositionUtil.isInRange(effect.getEffected(), effector, range, false))
						effector.getLifeStats().increaseHp(TYPE.HP, healValue, effect, LOG.REGULAR);
				} else {
					for (Player p : group.getOnlineMembers()) {
						if (PositionUtil.isInRange(effect.getEffected(), p, range, false))
							p.getLifeStats().increaseHp(TYPE.HP, healValue, effect, LOG.REGULAR);
					}
				}
			}
		});
	}
}
