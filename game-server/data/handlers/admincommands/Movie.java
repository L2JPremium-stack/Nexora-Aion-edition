package admincommands;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author d3v1an
 */
public class Movie extends AdminCommand {

	public Movie() {
		super("movie");

		// @formatter:off
		setSyntaxInfo(
			"<cutsceneId> - Plays the given cutscene (correct rendering depends on your current map)",
			"m <movieId> - Plays the given movie cutscene"
		);
		// @formatter:on
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			sendInfo(player);
			return;
		}
		boolean isCutsceneMovie = "m".equalsIgnoreCase(params[0]);
		int cutsceneId = Integer.parseInt(params[isCutsceneMovie ? 1 : 0]);
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(isCutsceneMovie, 0, 0, cutsceneId, true));
	}
}
