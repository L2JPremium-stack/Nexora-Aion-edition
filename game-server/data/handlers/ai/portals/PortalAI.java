package ai.portals;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.portal.PortalPath;
import com.nexora.gameserver.model.templates.teleport.TeleporterTemplate;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.services.teleport.PortalService;
import com.nexora.gameserver.services.teleport.TeleportService;

import ai.ActionItemNpcAI;

/**
 * @author xTz
 */
@AIName("portal")
public class PortalAI extends ActionItemNpcAI {

	protected TeleporterTemplate teleportTemplate;

	public PortalAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		return true;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		teleportTemplate = DataManager.TELEPORTER_DATA.getTeleporterTemplateByNpcId(getNpcId());
	}

	@Override
	protected void handleDialogStart(Player player) {
		QuestEngine.getInstance().onDialog(new QuestEnv(getOwner(), player, 0, DialogAction.USE_OBJECT));
		super.handleDialogStart(player);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		PortalPath portalPath = DataManager.PORTAL2_DATA.getPortalUsePath(getNpcId(), player);
		if (portalPath != null) {
			PortalService.port(portalPath, player, getOwner());
		} else if (teleportTemplate != null) {
			TeleportService.teleportToFirstTeleportLocation(player, getOwner(), TeleportAnimation.FADE_OUT_BEAM);
		} else {
			super.handleUseItemFinish(player);
		}
	}

}
