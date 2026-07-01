package ai.portals;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.nexora.gameserver.model.templates.teleport.TelelocationTemplate;
import com.nexora.gameserver.model.templates.teleport.TeleportLocation;
import com.nexora.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.services.trade.PricesService;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
@AIName("portal_request")
public class PortalRequestAI extends PortalAI {

	public PortalRequestAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		TeleportLocation firstLoc = teleportTemplate.getTeleLocIdData().getTelelocations().getFirst();
		TelelocationTemplate locationTemplate = DataManager.TELELOCATION_DATA.getTelelocationTemplate(firstLoc.getLocId());
		RequestResponseHandler<Npc> portal = new RequestResponseHandler<>(getOwner()) {

			@Override
			public void acceptRequest(Npc requester, Player responder) {
				TeleportService.teleport(responder, firstLoc, TeleportAnimation.JUMP_IN);
			}

		};
		long transportationPrice = PricesService.getPriceForService(firstLoc.getPrice(), player.getRace());
		if (player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_TELEPORT_NEED_CONFIRM, portal)) {
			PacketSendUtility.sendPacket(player,
				new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_TELEPORT_NEED_CONFIRM, getObjectId(), getObjectTemplate().getTalkDistance(), locationTemplate.getL10n(), transportationPrice));
		}
	}
}
