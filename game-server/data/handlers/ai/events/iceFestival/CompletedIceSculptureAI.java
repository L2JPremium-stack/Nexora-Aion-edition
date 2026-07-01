package ai.events.iceFestival;

import static com.nexora.gameserver.model.DialogAction.SETPRO1;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI;

@AIName("icefestival_completed_ice_sculpture")
public class CompletedIceSculptureAI extends GeneralNpcAI {

	public CompletedIceSculptureAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		// custom dialog handling because the quest doesn't show up in the quest selection (incomplete quest data in the 4.8 game client)
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		switch (dialogActionId) {
			case SETPRO1:
				SkillEngine.getInstance().getSkill(getOwner(), 22719, 1, player).useWithoutPropSkill(); // world_event_icebuff_drop
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
				return true;
		}
		return false;
	}
}
