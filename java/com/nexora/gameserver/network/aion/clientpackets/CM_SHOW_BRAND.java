package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.TemporaryPlayerTeam;
import com.nexora.gameserver.model.team.alliance.PlayerAlliance;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_SHOW_BRAND;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Sweetkr, Simple
 */
public class CM_SHOW_BRAND extends AionClientPacket {

	@SuppressWarnings("unused")
	private int action;
	private int brandId;
	private int targetObjectId;

	public CM_SHOW_BRAND(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		action = readD();
		brandId = readD();
		targetObjectId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		TemporaryPlayerTeam<?> team = player.getCurrentTeam();
		if (team == null) {
			PacketSendUtility.sendPacket(player, new SM_SHOW_BRAND(brandId, targetObjectId));
		} else if (team.isLeader(player) || team instanceof PlayerAlliance alliance && alliance.isSomeCaptain(player)) {
			team.updateBrand(brandId, targetObjectId);
		}
	}
}
