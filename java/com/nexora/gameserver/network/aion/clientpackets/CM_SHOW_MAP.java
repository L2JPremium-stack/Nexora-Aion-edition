package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.conquerorAndProtectorSystem.ConquerorAndProtectorService;

/**
 * @author Lyahim
 */
public class CM_SHOW_MAP extends AionClientPacket {

	private static final Logger log = LoggerFactory.getLogger(CM_SHOW_MAP.class);
	private byte action;

	public CM_SHOW_MAP(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		action = readC();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		switch (action) {
			case 0:
				ConquerorAndProtectorService.getInstance().intruderScan(player);
				break;
			case 1:
				// TODO unk
				break;
			default:
				log.warn(player + " sent unknown show map action type: " + action);
		}
	}
}
