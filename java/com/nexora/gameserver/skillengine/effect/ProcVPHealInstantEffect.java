package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAttribute;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.player.PlayerCommonData;
import com.nexora.gameserver.network.aion.serverpackets.SM_STATUPDATE_EXP;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author kecimis, source.com
 */
public class ProcVPHealInstantEffect extends EffectTemplate {

	@XmlAttribute(required = true)
	protected int value2;// cap
	@XmlAttribute
	protected boolean percent;

	@Override
	public void applyEffect(Effect effect) {
		if (effect.getEffected() instanceof Player) {
			Player player = (Player) effect.getEffected();
			PlayerCommonData pcd = player.getCommonData();

			long cap = pcd.getMaxReposeEnergy() * value2 / 100;

			if (pcd.isReadyForReposeEnergy() && pcd.getCurrentReposeEnergy() < cap) {
				int valueWithDelta = calculateBaseValue(effect);
				long addEnergy = 0;
				if (percent)
					addEnergy = (int) (pcd.getMaxReposeEnergy() * valueWithDelta * 0.001);// recheck when more skills
				else
					addEnergy = valueWithDelta;

				pcd.addReposeEnergy(addEnergy);
				PacketSendUtility.sendPacket(
					player,
					new SM_STATUPDATE_EXP(pcd.getExpShown(), pcd.getExpRecoverable(), pcd.getExpNeed(), pcd.getCurrentReposeEnergy(), pcd
						.getMaxReposeEnergy()));
			}
		}
	}

}
