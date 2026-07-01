package ai.worlds.panesterra;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.stats.AbyssRankEnum;

/**
 * @author Estrayl
 */
@AIName("rank_100_advance_corridor")
public class Rank100CorridorAI extends AdvanceCorridorAI {

	public Rank100CorridorAI(Npc owner) {
		super(owner);
		despawnInMin = 5;
	}

	@Override
	public void handleDialogStart(Player player) {
		if (player.getAbyssRank().getRank().ordinal() < AbyssRankEnum.STAR5_OFFICER.ordinal()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_TELEPOTER_GAB1_USER03());
			return;
		}
		super.handleDialogStart(player);
	}
}
