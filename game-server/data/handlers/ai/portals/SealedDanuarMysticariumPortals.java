package ai.portals;

import static com.nexora.gameserver.model.DialogAction.SETPRO1;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.portal.PortalPath;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.instance.InstanceService;
import com.nexora.gameserver.services.teleport.PortalService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.WorldMapInstance;

/**
 * Created by Yeats on 22.02.2016.
 */
@AIName("sealed_danuar_mysticarium_portal")
public class SealedDanuarMysticariumPortals extends PortalDialogAI {

	public SealedDanuarMysticariumPortals(Npc owner) {
		super(owner);
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		if (getNpcId() == 730721 || getNpcId() == 730722) {
			PortalPath portalPath = DataManager.PORTAL2_DATA.getPortalDialogPath(getNpcId(), dialogActionId, player);
			WorldMapInstance instance = InstanceService.getRegisteredInstance(300480000, player.getObjectId());
			if (instance == null) {
				if (!player.getPortalCooldownList().isPortalUseDisabled(300480000)) {
					if (player.getInventory().decreaseByItemId(185000223, 1)) {
						PortalService.port(portalPath, player, getOwner());
						return true;
					} else {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_CANT_ENTER_WITHOUT_ITEM());
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
						return true;
					}
				} else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_MAKE_INSTANCE_COOL_TIME());
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
					return true;
				}
			} else {
				PortalService.port(portalPath, player, getOwner());
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
				return true;
			}
		} else {
			if (!getOwner().isInInstance()) {
				return true;
			}
			if (getNpcId() == 731583) {
				if (dialogActionId == SETPRO1) {
					AIActions.handleUseItemFinish(this, player);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0, questId));
				}
			}
		}
		return true;
	}
}
