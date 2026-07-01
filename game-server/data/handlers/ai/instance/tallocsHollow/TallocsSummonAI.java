package ai.instance.tallocsHollow;

import static com.nexora.gameserver.model.DialogAction.MAKE_MERCENARY;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.CreatureType;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_CUSTOM_SETTINGS;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.network.aion.serverpackets.SM_TRANSFORM_IN_SUMMON;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
@AIName("tallocssummon")
public class TallocsSummonAI extends NpcAI {

	public TallocsSummonAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		if (dialogActionId != MAKE_MERCENARY || getOwner().getCreator() != null)
			return false;
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		getOwner().setCreatorId(player.getObjectId());
		getOwner().setMasterName(player.getName());
		getOwner().setState(CreatureState.ACTIVE, true);
		PacketSendUtility.sendPacket(player, new SM_TRANSFORM_IN_SUMMON(player, getObjectId()));
		PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getObjectId(), 0, CreatureType.FRIEND.getId(), 0));
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
		return true;
	}

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
	}
}
