package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.StaticDoorService;

/**
 * @author rhys2002 & Wakizashi
 */
public class CM_OPEN_STATICDOOR extends AionClientPacket {

	private int doorId;

	/**
	 * @param opcode
	 */
	public CM_OPEN_STATICDOOR(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		doorId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = this.getConnection().getActivePlayer();
		StaticDoorService.getInstance().openStaticDoor(player, doorId);
	}

}
