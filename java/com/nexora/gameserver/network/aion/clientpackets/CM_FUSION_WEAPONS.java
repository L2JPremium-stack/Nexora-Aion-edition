package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.ArmsfusionService;
import com.nexora.gameserver.utils.audit.AuditLogger;

/**
 * @author zdead, Wakizashi, Neon
 */
public class CM_FUSION_WEAPONS extends AionClientPacket {

	public CM_FUSION_WEAPONS(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	private int npcObjId;
	private int mainWeaponObjId;
	private int fuseWeaponObjId;

	@Override
	protected void readImpl() {
		npcObjId = readD();
		mainWeaponObjId = readD();
		fuseWeaponObjId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.isTargetingNpcWithFunction(npcObjId, DialogAction.COMPOUND_WEAPON))
			ArmsfusionService.fusionWeapons(getConnection().getActivePlayer(), mainWeaponObjId, fuseWeaponObjId);
		else
			AuditLogger.log(player, "tried to fuse weapons without targeting an armsfusion officer");
	}
}
