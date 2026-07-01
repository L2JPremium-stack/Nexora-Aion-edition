package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.configs.main.WorldConfig;
import com.nexora.gameserver.model.templates.world.WorldMapTemplate;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;
import com.nexora.gameserver.world.WorldPosition;

/**
 * @author ATracer
 */
public class SM_CHANNEL_INFO extends AionServerPacket {

	int instanceCount = 0;
	int currentChannel = 0;

	public SM_CHANNEL_INFO(WorldPosition position) {
		if (position == null || !position.isSpawned()) {
			instanceCount = 1;
			currentChannel = 1;
		} else {
			WorldMapTemplate template = position.getWorldMapInstance().getTemplate();
			if (position.getWorldMapInstance().isBeginnerInstance()) {
				instanceCount = template.getBeginnerTwinCount();
				if (WorldConfig.WORLD_EMULATE_FASTTRACK)
					instanceCount += template.getTwinCount();
				currentChannel = position.getInstanceId() - 1;
			} else {
				instanceCount = template.getTwinCount();
				if (WorldConfig.WORLD_EMULATE_FASTTRACK)
					instanceCount += template.getBeginnerTwinCount();
				currentChannel = position.getInstanceId() - 1;
			}
		}
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(currentChannel);
		writeD(instanceCount);
	}
}
