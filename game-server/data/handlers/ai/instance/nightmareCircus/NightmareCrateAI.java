package ai.instance.nightmareCircus;

import java.util.Collection;
import java.util.HashSet;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.configs.main.GroupConfig;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.drop.DropRegistrationService;
import com.nexora.gameserver.services.drop.DropService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.utils.audit.AuditLogger;

import ai.ActionItemNpcAI;

/**
 * @author Ritsu
 */
@AIName("nightmare_crate")
public class NightmareCrateAI extends ActionItemNpcAI {

	public NightmareCrateAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		int npcId = getOwner().getNpcId();
		if ((npcId == 831745)
			&& (player.getInventory().getItemCountByItemId(185000187) > 0 || player.getInventory().getItemCountByItemId(185000184) > 0)) {
			if (player.getInventory().decreaseByItemId(185000187, 1)) {
				analyzeOpening(player);
			} else if (player.getInventory().decreaseByItemId(185000184, 1)) {
				analyzeOpening(player);
			}
		} else if (npcId == 831575
			&& (player.getInventory().getItemCountByItemId(185000187) > 2 || player.getInventory().getItemCountByItemId(185000184) > 2)) {
			if (player.getInventory().decreaseByItemId(185000187, 3)) {
				analyzeOpening(player);
			} else if (player.getInventory().decreaseByItemId(185000184, 3)) {
				analyzeOpening(player);
			}
		} else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_IDEVENT01_TREASUREBOX());
		}
	}

	private void analyzeOpening(Player player) {
		if (getOwner().isInState(CreatureState.DEAD)) {
			AuditLogger.log(player, "attempted multiple chest looting!");
			return;
		}

		Collection<Player> players = new HashSet<>();
		if (player.isInGroup()) {
			for (Player member : player.getPlayerGroup().getOnlineMembers()) {
				if (PositionUtil.isInRange(member, getOwner(), GroupConfig.GROUP_MAX_DISTANCE)) {
					players.add(member);
				}
			}
		} else if (player.isInAlliance()) {
			for (Player member : player.getPlayerAlliance().getOnlineMembers()) {
				if (PositionUtil.isInRange(member, getOwner(), GroupConfig.GROUP_MAX_DISTANCE)) {
					players.add(member);
				}
			}
		} else {
			players.add(player);
		}
		DropRegistrationService.getInstance().registerDrop(getOwner(), player, getHighestLevel(players), players);
		AIActions.die(this, player);
		DropService.getInstance().requestDropList(player, getObjectId());
		super.handleUseItemFinish(player);
	}

	private int getHighestLevel(Collection<Player> players) {
		return players.stream().mapToInt(Player::getLevel).max().getAsInt();
	}
}
