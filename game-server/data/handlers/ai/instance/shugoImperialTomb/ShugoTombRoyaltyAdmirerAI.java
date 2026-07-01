package ai.instance.shugoImperialTomb;

import static com.nexora.gameserver.model.DialogAction.SETPRO1;
import static com.nexora.gameserver.model.DialogAction.SETPRO2;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.instance.StageList;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Ritsu
 */
@AIName("shugo_tomb_royalty_admirer")
public class ShugoTombRoyaltyAdmirerAI extends NpcAI {

	public ShugoTombRoyaltyAdmirerAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		int instanceId = player.getInstanceId();

		switch (dialogActionId) {
			case SETPRO1:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
				switch (getNpcId()) {
					case 831110:
						changeStageList(player, StageList.START_STAGE_1_PHASE_1);
						break;
					case 831111:
						changeStageList(player, StageList.START_STAGE_2_PHASE_1);
						break;
					case 831112:
						changeStageList(player, StageList.START_STAGE_3_PHASE_1);
						break;
					case 831114: // Crown Prince's Delighted Admirer
					case 831306: // Crown Prince's Disappointed Admirer
						TeleportService.teleportTo(player, 300560000, instanceId, 346.27332f, 424.07101f, 294.75793f, (byte) 90, TeleportAnimation.FADE_OUT_BEAM);
						break;
					case 831115: // Empress' Delighted Admirer
					case 831195: // Empress' Disappointed Admirer
						TeleportService.teleportTo(player, 300560000, instanceId, 450.8527f, 105.94637f, 212.20023f, (byte) 90, TeleportAnimation.FADE_OUT_BEAM);
						break;
				}
				break;
			case SETPRO2:
				switch (getNpcId()) {
					case 831114: // Crown Prince's Delighted Admirer
					case 831306: // Crown Prince's Disappointed Admirer
						SkillEngine.getInstance().applyEffectDirectly(player.getRace() == Race.ASMODIANS ? 21104 : 21095, player, player);
						break;
					case 831115: // Empress' Delighted Admirer
					case 831195: // Empress' Disappointed Admirer
						SkillEngine.getInstance().applyEffectDirectly(player.getRace() == Race.ASMODIANS ? 21105 : 21096, player, player);
						break;
				}
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1012));
		}
		return true;
	}

	private void changeStageList(Player player, StageList stageList) {
		getPosition().getWorldMapInstance().getInstanceHandler().onChangeStageList(stageList);
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		AIActions.deleteOwner(this);
	}
}
