package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.model.DialogPage;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.house.HouseRegistry;
import com.nexora.gameserver.model.templates.housing.HousingPostbox;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.network.aion.serverpackets.SM_OBJECT_USE_UPDATE;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.player.PlayerMailboxState;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Rolandas, Neon
 */
public class PostboxObject extends UseableHouseObject<HousingPostbox> {

	public PostboxObject(HouseRegistry registry, int objId, int templateId) {
		super(registry, objId, templateId);
	}

	@Override
	public void onUse(final Player player) {
		if (!setOccupant(player)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_OBJECT_OCCUPIED_BY_OTHER());
			return;
		}

		player.getMailbox().mailBoxState = PlayerMailboxState.REGULAR;
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_OBJECT_USE(getObjectTemplate().getL10n()));
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), DialogPage.MAIL.id()));
		PacketSendUtility.sendPacket(player, new SM_OBJECT_USE_UPDATE(player.getObjectId(), 0, 0, this));
	}
}
