package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.legionDominion.LegionDominionLocation;
import com.nexora.gameserver.model.team.legion.Legion;
import com.nexora.gameserver.model.team.legion.LegionEmblem;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;
import com.nexora.gameserver.services.LegionDominionService;
import com.nexora.gameserver.services.LegionService;

/**
 * @author Neon, Yeats
 */
public class SM_LEGION_DOMINION_LOC_INFO extends AionServerPacket {

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(LegionDominionService.getInstance().getLegionDominions().size());
		for (LegionDominionLocation loc : LegionDominionService.getInstance().getLegionDominions()) {
			Legion legion = loc.getLegionId() == 0 ? null : LegionService.getInstance().getLegion(loc.getLegionId());
			LegionEmblem emblem = legion == null ? new LegionEmblem() : legion.getLegionEmblem();
			writeD(loc.getLocationId());
			writeD(loc.getLegionId());
			writeC(emblem.getEmblemId());
			writeC(emblem.getEmblemType().getValue());
			writeC(emblem.getColor_a());
			writeC(emblem.getColor_r());
			writeC(emblem.getColor_g());
			writeC(emblem.getColor_b());
			writeS(legion == null ? null : legion.getName());
		}
	}
}
