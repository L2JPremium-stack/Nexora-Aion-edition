package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;

/**
 * @author xTz
 */
public class CM_STOP_TRAINING extends AionClientPacket {

	public CM_STOP_TRAINING(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		// nothing to read
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		player.getPosition().getWorldMapInstance().getInstanceHandler().onStopTraining(player);
	}
}
