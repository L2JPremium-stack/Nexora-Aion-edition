package com.nexora.gameserver.controllers;

import com.nexora.gameserver.model.gameobjects.HouseObject;
import com.nexora.gameserver.model.gameobjects.UseableHouseObject;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.housing.PlaceableHouseObject;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.PositionUtil;

/**
 * @author Rolandas
 */
public class PlaceableObjectController<T extends PlaceableHouseObject> extends VisibleObjectController<HouseObject<T>> {

	@Override
	public void onDespawn() {
		super.onDespawn();
		getOwner().onDespawn();
	}

	public void onDialogRequest(Player player) {
		if (!PositionUtil.isInTalkRange(player, getOwner())) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_OBJECT_TOO_FAR_TO_USE());
			return;
		}
		getOwner().onDialogRequest(player);
	}

	@Override
	public void notKnow(VisibleObject object) {
		super.notKnow(object);
		if (getOwner() instanceof UseableHouseObject<?> useableHouseObject && object instanceof Player player)
			useableHouseObject.releaseOccupant(player);
	}
}
