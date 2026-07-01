package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Friend;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_FRIEND_LIST;
import com.nexora.gameserver.network.aion.serverpackets.SM_UPDATE_NOTE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.World;

/**
 * Received when a player sets his note
 * 
 * @author Ben
 */
public class CM_SET_NOTE extends AionClientPacket {

	private String note;

	public CM_SET_NOTE(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		note = readS();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (note.equals(player.getCommonData().getNote()))
			return;
		player.getCommonData().setNote(note);
		for (Friend friend : player.getFriendList()) {
			Player friendPlayer = World.getInstance().getPlayer(friend.getObjectId());
			if (friendPlayer != null)
				PacketSendUtility.sendPacket(friendPlayer, new SM_FRIEND_LIST());
		}
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_UPDATE_NOTE(player));
	}
}
