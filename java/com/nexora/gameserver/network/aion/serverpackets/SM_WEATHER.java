package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.templates.world.WeatherEntry;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer, Kwazar, Nemesiss
 */
public class SM_WEATHER extends AionServerPacket {

	private WeatherEntry[] weatherEntries;

	public SM_WEATHER(WeatherEntry[] weatherEntries) {
		this.weatherEntries = weatherEntries;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(0x00);// unk
		writeC(weatherEntries.length);
		for (WeatherEntry entry : weatherEntries)
			writeC(entry.getCode());
	}
}
