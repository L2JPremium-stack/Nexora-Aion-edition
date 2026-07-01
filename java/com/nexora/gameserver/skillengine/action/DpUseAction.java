package com.nexora.gameserver.skillengine.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.skillengine.model.Skill;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DpUseAction")
public class DpUseAction extends Action {

	@XmlAttribute(required = true)
	protected int value;

	@Override
	public boolean act(Skill skill) {
		Player effector = (Player) skill.getEffector();
		int currentDp = effector.getCommonData().getDp();

		if (currentDp <= 0 || currentDp < value) {
			PacketSendUtility.sendPacket(effector, SM_SYSTEM_MESSAGE.STR_SKILL_NOT_ENOUGH_DP());
			return false;
		}
		effector.getCommonData().setDp(currentDp - value);
		return true;
	}
}
