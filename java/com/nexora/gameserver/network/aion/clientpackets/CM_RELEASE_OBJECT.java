package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.PostboxObject;
import com.nexora.gameserver.model.gameobjects.UseableHouseObject;
import com.nexora.gameserver.model.gameobjects.UseableItemObject;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Rolandas, Neon
 */
public class CM_RELEASE_OBJECT extends AionClientPacket {

	int targetObjectId;

	public CM_RELEASE_OBJECT(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		targetObjectId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		VisibleObject object = player.getKnownList().getObject(targetObjectId);
		if (object instanceof UseableHouseObject<?> useableHouseObject && useableHouseObject.releaseOccupant(player)) { // release object
			if (player.getController().hasScheduledTask(TaskId.HOUSE_OBJECT_USE) || object instanceof PostboxObject) { // post box always sends the message
				if (object instanceof UseableItemObject) // reset visual use progress bar
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), object.getObjectId(), 0, 9));
				sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_OBJECT_CANCEL_USE());
			}
			player.getController().cancelTask(TaskId.HOUSE_OBJECT_USE);
		}
	}
}
