package com.nexora.gameserver.skillengine.condition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.items.ItemSlot;
import com.nexora.gameserver.skillengine.model.Skill;

/**
 * @author Rolandas, Cheatkiller
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChargeArmorCondition")
public class ChargeArmorCondition extends ChargeCondition {

	@Override
	public boolean validate(Skill env) {
		if (env.getEffector() instanceof Player effector) {
			for (Item item : effector.getEquipment().getEquippedItems()) {
				if (item.getItemTemplate().isArmor() && item.getConditioningInfo() != null) {
					if ((item.getEquipmentSlot() & ItemSlot.SUB_OFF_HAND.getSlotIdMask()) != 0)
						continue;
					item.getConditioningInfo().updateChargePoints(-value);
				}
			}
		}
		return true;
	}
}
