package com.nexora.gameserver.skillengine.effect;

import com.nexora.gameserver.controllers.observer.ActionObserver;
import com.nexora.gameserver.controllers.observer.ObserverType;
import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.item.enums.EquipType;
import com.nexora.gameserver.network.aion.serverpackets.SM_RIDE_ROBOT;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Rolandas, Cheatkiller
 */
public class RideRobotEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(Effect effect) {
		Player player = (Player) effect.getEffected();
		player.setRobotId(player.getEquipment().getMainHandWeapon().getItemSkinTemplate().getRobotId());
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_RIDE_ROBOT(player));

		effect.addObserver(player, new ActionObserver(ObserverType.UNEQUIP) {

			@Override
			public void unequip(Item item, Player owner) {
				if (item.getEquipmentType() == EquipType.WEAPON) {
					effect.endEffect();
				}
			}
		});
	}

	@Override
	public void endEffect(Effect effect) {
		Player player = (Player) effect.getEffected();
		player.setRobotId(0);
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_RIDE_ROBOT(player));
		for (Effect ef : player.getEffectController().getAbnormalEffects()) {
			if (ef.getSkillTemplate().getRideRobotCondition() != null)
				ef.endEffect();
		}
	}
}
