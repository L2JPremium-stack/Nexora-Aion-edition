package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.player.PlayerScripts;
import com.nexora.gameserver.model.house.House;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_HOUSE_SCRIPTS;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.audit.AuditLogger;

/**
 * @author Rolandas, Neon, Sykra
 */
public class CM_HOUSE_SCRIPT extends AionClientPacket {

	private int address;
	private int scriptId;
	private int totalSize;
	private int compressedSize;
	private int uncompressedSize;
	private byte[] scriptContent;

	public CM_HOUSE_SCRIPT(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		address = readD();
		scriptId = readUC();
		totalSize = readUH();
		if (totalSize > 0) {
			compressedSize = readD();
			if (compressedSize <= SM_HOUSE_SCRIPTS.MAX_COMPRESSED_SCRIPT_SIZE) {
				uncompressedSize = readD();
				scriptContent = readB(compressedSize);
			}
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (compressedSize > SM_HOUSE_SCRIPTS.MAX_COMPRESSED_SCRIPT_SIZE) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_SCRIPT_OVERFLOW());
			return;
		}
		House house = player.getActiveHouse();
		if (house == null || house.getAddress().getId() != address) {
			AuditLogger.log(player, "tried to modify script of house they don't own (address " + address + ")");
			return;
		}
		PlayerScripts scripts = house.getPlayerScripts();
		if (totalSize == 0)
			scripts.remove(scriptId);
		else
			scripts.set(scriptId, scriptContent, uncompressedSize);
		PacketSendUtility.broadcastPacket(player, new SM_HOUSE_SCRIPTS(address, scripts.get(scriptId)));
	}
}
