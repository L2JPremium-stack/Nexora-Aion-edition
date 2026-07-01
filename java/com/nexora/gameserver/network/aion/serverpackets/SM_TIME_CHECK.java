package com.nexora.gameserver.network.aion.serverpackets;

import java.lang.management.ManagementFactory;

import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * I have no idea wtf is this
 * 
 * @author -Nemesiss-
 */
public class SM_TIME_CHECK extends AionServerPacket {

	private int serverUpTime, nanoTime;

	public SM_TIME_CHECK(int nanoTime) {
		this.serverUpTime = (int) ManagementFactory.getRuntimeMXBean().getUptime();
		this.nanoTime = nanoTime;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(serverUpTime);
		writeD(nanoTime);
	}
}
