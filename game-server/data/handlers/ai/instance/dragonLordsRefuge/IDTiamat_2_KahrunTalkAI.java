package ai.instance.dragonLordsRefuge;

import static com.nexora.gameserver.model.DialogAction.SETPRO1;

import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.CreatureType;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author bobobear, Estrayl
 */
@AIName("IDTiamat_2_Kahrun_Talk")
public class IDTiamat_2_KahrunTalkAI extends NpcAI {

	private final AtomicBoolean isActivated = new AtomicBoolean();

	public IDTiamat_2_KahrunTalkAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		if (getPosition().getWorldMapInstance().getNpcs(730625).isEmpty())
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex) {
		if (dialogActionId == SETPRO1) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
			if (isActivated.compareAndSet(false, true)) {
				getOwner().overrideNpcType(CreatureType.PEACE);
				PacketSendUtility.broadcastMessage(getOwner(), 1500604, 500);
				ThreadPoolManager.getInstance().schedule(() -> {
					getMoveController().moveToPoint(500f, 516.6f, 240.27f);
					setStateIfNot(AIState.WALKING);
					getOwner().setState(CreatureState.ACTIVE, true);
					PacketSendUtility.broadcastToMap(getOwner(), new SM_EMOTION(getOwner(), EmotionType.WALK));
					ThreadPoolManager.getInstance().schedule(this::spawnOrb, 1500);
				}, 2000);
			}
		}
		return true;
	}

	private void spawnOrb() {
		spawn(730625, 503.2197f, 516.6517f, 242.6040f, (byte) 0, 4);
		AIActions.deleteOwner(this);
	}
}
