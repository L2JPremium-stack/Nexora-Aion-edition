package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.ArmsfusionService;
import com.nexora.gameserver.utils.audit.AuditLogger;

/**
 * @author zdead
 */
public class CM_BREAK_WEAPONS extends AionClientPacket {

	public CM_BREAK_WEAPONS(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	private int npcObjId;
	private int weaponObjId;

	@Override
	protected void readImpl() {
		npcObjId = readD();
		weaponObjId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.isTargetingNpcWithFunction(npcObjId, DialogAction.DECOMPOUND_WEAPON))
			ArmsfusionService.breakWeapons(player, weaponObjId);
		else
			AuditLogger.log(player, "tried to defuse a weapon without targeting an armsfusion officer");
	}
}
