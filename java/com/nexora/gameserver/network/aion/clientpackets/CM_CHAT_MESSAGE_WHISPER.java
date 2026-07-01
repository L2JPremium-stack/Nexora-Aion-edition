package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.configs.main.CustomConfig;
import com.nexora.gameserver.model.ChatType;
import com.nexora.gameserver.model.gameobjects.player.CustomPlayerState;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.restrictions.PlayerRestrictions;
import com.nexora.gameserver.services.NameRestrictionService;
import com.nexora.gameserver.services.player.PlayerChatService;
import com.nexora.gameserver.utils.ChatUtil;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.World;

/**
 * Packet that reads Whisper chat messages.<br>
 * 
 * @author SoulKeeper
 */
public class CM_CHAT_MESSAGE_WHISPER extends AionClientPacket {

	/**
	 * To whom this message is sent
	 */
	private String name;

	/**
	 * Message text
	 */
	private String message;

	/**
	 * Constructs new client packet instance.
	 * 
	 * @param opcode
	 */
	public CM_CHAT_MESSAGE_WHISPER(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	/**
	 * Read message
	 */
	@Override
	protected void readImpl() {
		name = readS();
		message = readS();
	}

	@Override
	protected void runImpl() {
		String realName = ChatUtil.getRealCharName(name);
		Player sender = getConnection().getActivePlayer();
		Player receiver = World.getInstance().getPlayer(realName);

		if (receiver == null) {
			sendPacket(SM_SYSTEM_MESSAGE.STR_NO_SUCH_USER(realName));
		} else if (receiver.isInCustomState(CustomPlayerState.NO_WHISPERS_MODE) && !sender.isStaff()) {
			sendPacket(SM_SYSTEM_MESSAGE.STR_WHISPER_REFUSE(receiver.getName(true)));
		} else if (sender.getLevel() < CustomConfig.LEVEL_TO_WHISPER && !receiver.isStaff()) {
			sendPacket(SM_SYSTEM_MESSAGE.STR_CANT_WHISPER_LEVEL(String.valueOf(CustomConfig.LEVEL_TO_WHISPER)));
		} else if (receiver.getBlockList().contains(sender.getObjectId())) {
			sendPacket(SM_SYSTEM_MESSAGE.STR_YOU_EXCLUDED(receiver.getName()));
		} else if (sender.getRace() != receiver.getRace() && !CustomConfig.SPEAKING_BETWEEN_FACTIONS && !sender.isStaff() && !receiver.isStaff()) {
			sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_CANT_WHISPER_OTHER_RACE());
		} else {
			if (!PlayerRestrictions.canChat(sender))
				return;
			PlayerChatService.logWhisper(sender, receiver, message);
			PacketSendUtility.sendPacket(receiver, new SM_MESSAGE(sender, NameRestrictionService.filterMessage(message), ChatType.WHISPER));
		}
	}
}
