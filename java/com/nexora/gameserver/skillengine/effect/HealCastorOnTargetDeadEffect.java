package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.nexora.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.HealType;
import com.nexora.gameserver.utils.PositionUtil;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealCastorOnTargetDeadEffect")
public class HealCastorOnTargetDeadEffect extends EffectTemplate {

	@XmlAttribute
	protected HealType type;// useless
	@XmlAttribute
	protected float range;
	@XmlAttribute
	protected boolean healparty;

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void endEffect(Effect effect) {
		Creature effected = effect.getEffected();
		Creature effector = effect.getEffector();
		if (effected.isDead()) {
			int healValue = calculateBaseValue(effect);
			var group = healparty && effector instanceof Player p ? p.getCurrentGroup() : null;
			if (group == null) {
				if (PositionUtil.isInRange(effected, effector, range, false))
					effector.getLifeStats().increaseHp(TYPE.HP, healValue, effect, LOG.REGULAR);
			} else {
				for (Player p : group.getOnlineMembers()) {
					if (PositionUtil.isInRange(effected, p, range, false))
						effector.getLifeStats().increaseHp(TYPE.HP, healValue, effect, LOG.REGULAR);
				}
			}
		}
	}
}
