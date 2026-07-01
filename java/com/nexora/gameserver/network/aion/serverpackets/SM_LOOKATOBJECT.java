package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author alexa026
 */
public class SM_LOOKATOBJECT extends AionServerPacket {

	private VisibleObject visibleObject;
	private int targetObjectId;
	private int heading;

	public SM_LOOKATOBJECT(VisibleObject visibleObject) {
		this.visibleObject = visibleObject;
		this.targetObjectId = visibleObject.getTarget() == null ? 0 : visibleObject.getTarget().getObjectId();
		this.heading = visibleObject.getHeading();
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(visibleObject.getObjectId());
		writeD(targetObjectId);
		writeC(heading);
	}
}
