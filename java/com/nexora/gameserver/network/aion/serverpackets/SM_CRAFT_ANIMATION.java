package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Mr. Poke
 */
public class SM_CRAFT_ANIMATION extends AionServerPacket {

	private int playerObjId;
	private int targetObjectId;
	private int skillId;
	private int action;

	public SM_CRAFT_ANIMATION(int playerObjId, int targetObjectId, int skillId, int action) {
		this.playerObjId = playerObjId;
		this.targetObjectId = targetObjectId;
		this.skillId = skillId;
		this.action = action;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(playerObjId);
		writeD(targetObjectId);
		writeH(skillId);
		writeC(action);
	}
}
