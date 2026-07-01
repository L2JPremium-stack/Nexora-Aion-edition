package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;
import com.nexora.gameserver.world.zone.ZoneName;

/**
 * @author Rolandas
 */
public class SM_PLAYER_REGION extends AionServerPacket {

	private int playerObjId;
	private ZoneName subZone;

	public SM_PLAYER_REGION(Player player, ZoneName subZone) {
		this.playerObjId = player.getObjectId();
		this.subZone = subZone; // player.getActiveRegion().getZones(player).stream().findFirst().get().getAreaTemplate().getZoneName()
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(playerObjId);
		writeC(0);
		writeC(0);
		writeC(0);
		writeD(subZone.name().hashCode());
	}
}
