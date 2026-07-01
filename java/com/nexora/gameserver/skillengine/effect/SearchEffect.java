package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.state.CreatureSeeState;
import com.nexora.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Sweetkr
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchEffect")
public class SearchEffect extends EffectTemplate {

	@XmlAttribute
	protected CreatureSeeState state;

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void endEffect(Effect effect) {
		Creature effected = effect.getEffected();

		effected.unsetSeeState(state);
		effected.updateKnownlist();

		PacketSendUtility.broadcastPacketAndReceive(effected, new SM_PLAYER_STATE(effected));
	}

	@Override
	public void startEffect(final Effect effect) {
		Creature effected = effect.getEffected();

		effected.setSeeState(state);
		effected.updateKnownlist();

		PacketSendUtility.broadcastPacketAndReceive(effected, new SM_PLAYER_STATE(effected));
	}
}
