package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Sweetkr
 */
public class SM_SKILL_CANCEL extends AionServerPacket {

	private Creature creature;
	private int skillId;

	public SM_SKILL_CANCEL(Creature creature, int skillId) {
		this.creature = creature;
		this.skillId = skillId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(creature.getObjectId());
		writeH(skillId);
	}
}
