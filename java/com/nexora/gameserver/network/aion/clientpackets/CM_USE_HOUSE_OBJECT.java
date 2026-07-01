package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.HouseObject;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.world.World;

/**
 * @author Rolandas
 */
public class CM_USE_HOUSE_OBJECT extends AionClientPacket {

	int itemObjectId;

	public CM_USE_HOUSE_OBJECT(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		itemObjectId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null)
			return;

		VisibleObject visObject = World.getInstance().findVisibleObject(itemObjectId);
		if (visObject == null)
			return;
		if (visObject instanceof HouseObject<?>) {
			((HouseObject<?>) visObject).getController().onDialogRequest(player);
		}
	}

}
