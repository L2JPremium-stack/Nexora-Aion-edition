package consolecommands;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.legion.Legion;
import com.nexora.gameserver.model.team.legion.LegionMember;
import com.nexora.gameserver.network.aion.serverpackets.SM_GM_SHOW_LEGION_INFO;
import com.nexora.gameserver.network.aion.serverpackets.SM_GM_SHOW_LEGION_MEMBERLIST;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.ConsoleCommand;
import com.nexora.gameserver.utils.collections.FixedElementCountSplitList;
import com.nexora.gameserver.utils.collections.SplitList;
import com.nexora.gameserver.world.World;

/**
 * @author Yeats
 */
public class Guild extends ConsoleCommand {

	public Guild() {
		super("guild", "Displays info about given player's legion.");
	}

	@Override
	protected void execute(Player admin, String... params) {
		Player target = params.length > 0 ? World.getInstance().getPlayer(params[0]) : null;
		if (target != null) {
			Legion legion = target.getLegion();
			if (target.getLegion() != null) {
				PacketSendUtility.sendPacket(admin, new SM_GM_SHOW_LEGION_INFO(legion));
				SplitList<LegionMember> legionMemberSplitList = new FixedElementCountSplitList<>(legion.getMembers(), true, 80);
				legionMemberSplitList.forEach(part -> PacketSendUtility.sendPacket(admin,
					new SM_GM_SHOW_LEGION_MEMBERLIST(part, part.isFirst(), part.isLast())));
			}
		}
	}
}
