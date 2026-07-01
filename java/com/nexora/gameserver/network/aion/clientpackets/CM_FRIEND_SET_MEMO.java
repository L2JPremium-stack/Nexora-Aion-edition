package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Friend;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.SocialService;

/**
 * @author ginho1
 */
public class CM_FRIEND_SET_MEMO extends AionClientPacket {

	private String targetName;
	private String memo;

	public CM_FRIEND_SET_MEMO(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		targetName = readS();
		memo = readS();
	}

	@Override
	protected void runImpl() {
		Player activePlayer = getConnection().getActivePlayer();
		Friend friend = activePlayer.getFriendList().getFriend(targetName);
		if (friend == null) {
			sendPacket(SM_SYSTEM_MESSAGE.STR_BUDDYLIST_NOT_IN_LIST());
		} else {
			SocialService.setFriendMemo(activePlayer, friend, memo);
		}
	}
}
