package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Cheatkiller
 */
public class SM_RIDE_ROBOT extends AionServerPacket {

	private int robotId;
	private int objectId;

	public SM_RIDE_ROBOT(Player player) {
		this(player, player.getRobotId());
	}

	public SM_RIDE_ROBOT(Player player, int robotId) {
		this.objectId = player.getObjectId();
		this.robotId = robotId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(objectId);
		writeD(robotId);
	}
}
