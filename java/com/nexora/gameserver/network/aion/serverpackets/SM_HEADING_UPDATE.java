package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Nemesiss
 */
public class SM_HEADING_UPDATE extends AionServerPacket {

	private int objectId;
	private byte heading;

	public SM_HEADING_UPDATE(VisibleObject target) {
		this.objectId = target.getObjectId();
		this.heading = target.getHeading();
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(objectId);
		writeC(heading);
	}
}
