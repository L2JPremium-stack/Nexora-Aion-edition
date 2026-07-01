package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.item.ItemSocketService;
import com.nexora.gameserver.utils.PositionUtil;

/**
 * @author ATracer
 */
public class CM_GODSTONE_SOCKET extends AionClientPacket {

	private int npcObjectId;
	private int weaponId;
	private int stoneId;

	public CM_GODSTONE_SOCKET(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		npcObjectId = readD();
		weaponId = readD();
		stoneId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		VisibleObject npc = player.getTarget();
		if (npc instanceof Npc && npc.getObjectId() == npcObjectId && PositionUtil.isInTalkRange(player, (Npc) npc))
			ItemSocketService.socketGodstone(player, player.getEquipment().getEquippedItemByObjId(weaponId), stoneId);
	}
}
