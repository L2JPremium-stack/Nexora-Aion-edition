package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer
 */
public class SM_SUMMON_OWNER_REMOVE extends AionServerPacket {

	private int summonObjId;

	public SM_SUMMON_OWNER_REMOVE(int summonObjId) {
		this.summonObjId = summonObjId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(summonObjId);
	}
}
