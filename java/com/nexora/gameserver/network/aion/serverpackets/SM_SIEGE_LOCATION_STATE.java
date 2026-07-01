package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.siege.SiegeLocation;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author Source
 */
public class SM_SIEGE_LOCATION_STATE extends AionServerPacket {

	private int locationId;
	private int state;

	public SM_SIEGE_LOCATION_STATE(SiegeLocation location) {
		this.locationId = location.getLocationId();
		this.state = location.isVulnerable() ? 1 : 0;
	}

	public SM_SIEGE_LOCATION_STATE(int locationId, int state) {
		this.locationId = locationId;
		this.state = state;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(locationId);
		writeC(state);
	}

}
