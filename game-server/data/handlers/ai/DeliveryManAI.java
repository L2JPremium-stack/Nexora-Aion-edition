package ai;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.DialogPage;
import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.services.player.PlayerMailboxState;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.world.World;

/**
 * @author -Nemesiss-, Neon
 */
@AIName("deliveryman")
public class DeliveryManAI extends FollowingNpcAI {

	private static final int SERVICE_TIME = 5 * 60 * 1000;

	public DeliveryManAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		Player owner = getPlayer();
		getOwner().getController().addTask(TaskId.DESPAWN,
			ThreadPoolManager.getInstance().schedule(() -> AIActions.deleteOwner(DeliveryManAI.this), SERVICE_TIME));
		handleFollowMe(owner);
		handleCreatureMoved(owner);
		PacketSendUtility.broadcastMessage(getOwner(), 390266, 1500); // Here is your mail, akakak!
		PacketSendUtility.broadcastMessage(getOwner(), 390268, 30000); // Time is silver my friend, akakak!
	}

	@Override
	protected void handleDespawned() {
		PacketSendUtility.broadcastMessage(getOwner(), 390267); // Whiririkk, let's go!
		Player player = World.getInstance().getPlayer(getOwner().getCreatorId());
		if (player != null && getOwner().equals(player.getPostman()))
			player.setPostman(null);
		super.handleDespawned();
	}

	@Override
	protected void handleDialogStart(Player player) {
		if (player.equals(getPlayer())) {
			player.getMailbox().mailBoxState = PlayerMailboxState.EXPRESS;
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), DialogPage.MAIL.id()));
		} else {
			PacketSendUtility.broadcastMessage(getOwner(), 390269); // There is no mail for you, nyerk.
		}
	}

	private Player getPlayer() {
		return getOwner().getPosition().getWorldMapInstance().getPlayer(getOwner().getCreatorId());
	}
}
