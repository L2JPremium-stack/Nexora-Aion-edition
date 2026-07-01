package com.nexora.gameserver.network.loginserver.clientpackets;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.loginserver.LsClientPacket;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.World;

/**
 * @author Watson
 */
public class CM_BAN_RESPONSE extends LsClientPacket {

	public CM_BAN_RESPONSE(int opCode) {
		super(opCode);
	}

	private byte type;
	private int accountId;
	private String ip;
	private int time;
	private int adminObjId;
	private boolean result;

	@Override
	public void readImpl() {
		this.type = readC();
		this.accountId = readD();
		this.ip = readS();
		this.time = readD();
		this.adminObjId = readD();
		this.result = readUC() == 1;
	}

	@Override
	public void runImpl() {
		Player admin = World.getInstance().getPlayer(adminObjId);

		if (admin == null) {
			return;
		}

		// Some messages stuff
		String message;
		if (type == 1 || type == 3) {
			if (result) {
				if (time < 0)
					message = "Account ID " + accountId + " was successfully unbanned";
				else if (time == 0)
					message = "Account ID " + accountId + " was successfully banned";
				else
					message = "Account ID " + accountId + " was successfully banned for " + time + " minutes";
			} else
				message = "Error occurred while banning player's account";
			PacketSendUtility.sendMessage(admin, message);
		}
		if (type == 2 || type == 3) {
			if (result) {
				if (time < 0)
					message = "IP mask " + ip + " was successfully removed from block list";
				else if (time == 0)
					message = "IP mask " + ip + " was successfully added to block list";
				else
					message = "IP mask " + ip + " was successfully added to block list for " + time + " minutes";
			} else
				message = "Error occurred while adding IP mask " + ip;
			PacketSendUtility.sendMessage(admin, message);
		}
	}
}
