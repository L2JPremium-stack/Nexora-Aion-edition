package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexora.gameserver.model.gameobjects.player.DeniedStatus;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.alliance.PlayerAllianceService;
import com.nexora.gameserver.model.team.group.PlayerGroupService;
import com.nexora.gameserver.model.team.league.LeagueService;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.ChatUtil;
import com.nexora.gameserver.world.World;

/**
 * @author Lyahim, ATracer, Simple, Neon
 */
public class CM_INVITE_TO_GROUP extends AionClientPacket {

	private static final Logger log = LoggerFactory.getLogger(CM_INVITE_TO_GROUP.class);
	private String playerName;
	private int inviteType;

	public CM_INVITE_TO_GROUP(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		inviteType = readUC();
		playerName = readS();
	}

	@Override
	protected void runImpl() {
		final Player inviter = getConnection().getActivePlayer();
		if (inviter.isDead()) {
			sendPacket(SM_SYSTEM_MESSAGE.STR_PARTY_CANT_INVITE_WHEN_DEAD());
			return;
		}

		final Player invited = World.getInstance().getPlayer(ChatUtil.getRealCharName(playerName));
		if (invited == null) {
			sendPacket(SM_SYSTEM_MESSAGE.STR_NO_SUCH_USER(playerName));
			return;
		}

		if (invited.getPlayerSettings().isInDeniedStatus(DeniedStatus.GROUP)) {
			sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_INVITE_PARTY(invited.getName(true)));
			return;
		}

		switch (inviteType) {
			case 0:
				PlayerGroupService.inviteToGroup(inviter, invited);
				break;
			case 12: // 2.5
				PlayerAllianceService.inviteToAlliance(inviter, invited);
				break;
			case 28:
				LeagueService.inviteToLeague(inviter, invited);
				break;
			default:
				log.warn("Received unknown invite type from player " + inviter.getName() + ": " + inviteType);
				break;
		}
	}
}
