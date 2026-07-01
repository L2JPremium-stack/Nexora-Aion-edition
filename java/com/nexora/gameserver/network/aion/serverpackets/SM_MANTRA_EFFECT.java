package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Sweetkr
 */
public class SM_MANTRA_EFFECT extends AionServerPacket {

	private Creature effector;
	private int subEffectId;

	public SM_MANTRA_EFFECT(Creature effector, int subEffectId) {
		this.effector = effector;
		this.subEffectId = subEffectId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(0x00);// unk
		writeD(effector.getObjectId());
		writeH(subEffectId);
	}
}
