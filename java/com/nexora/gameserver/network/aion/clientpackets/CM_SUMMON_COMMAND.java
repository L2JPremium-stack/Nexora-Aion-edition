package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.Summon;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.summons.SummonMode;
import com.nexora.gameserver.model.summons.UnsummonType;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.summons.SummonsService;

/**
 * @author ATracer
 */
public class CM_SUMMON_COMMAND extends AionClientPacket {

	private int mode;
	private int targetObjId;

	public CM_SUMMON_COMMAND(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		mode = readUC();
		readD(); // 0
		readD(); // 0
		targetObjId = readD();
	}

	@Override
	protected void runImpl() {
		Player activePlayer = getConnection().getActivePlayer();
		Summon summon = activePlayer.getSummon();
		SummonMode summonMode = SummonMode.getSummonModeById(mode);
		if (summon != null && summonMode != null) {
			SummonsService.doMode(summonMode, summon, targetObjId, UnsummonType.COMMAND);
		}
	}
}
