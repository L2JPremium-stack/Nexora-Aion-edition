package ai.instance.tiamatStrongHold;

import static com.nexora.gameserver.model.DialogAction.SETPRO1;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Cheatkiller
 */
@AIName("kharun")
public class KharunAI extends NpcAI {

	public KharunAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		if (dialogActionId == SETPRO1) {
			AIActions.deleteOwner(this);
			startKharunEvent();
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}

	private void startKharunEvent() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				Npc aethericField = getPosition().getWorldMapInstance().getNpc(730613);
				Npc strongholdDoor = getPosition().getWorldMapInstance().getNpc(730612);
				Npc Kharun = (Npc) spawn(800335, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 60);
				Kharun.setTarget(aethericField);
				SkillEngine.getInstance().getSkill(Kharun, 20943, 60, aethericField).useNoAnimationSkill();
				PacketSendUtility.broadcastMessage(Kharun, 1500597, 1000);
				PacketSendUtility.broadcastMessage(Kharun, 1500598, 5000);
				strongholdDoor.getController().die();
				aethericField.getController().delete();
			}
		}, 3000);
	}
}
