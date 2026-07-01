package ai.worlds.panesterra.ahserionsflight;

import java.util.concurrent.Future;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Estrayl
 */
@AIName("ereshkigals_voice")
public class EreshkigalsVoiceAI extends NpcAI {

	private Future<?> idleTask;

	public EreshkigalsVoiceAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		idleTask = ThreadPoolManager.getInstance().schedule(() -> {
			PacketSendUtility.broadcastMessage(getOwner(), 1501160); // I won't forgive you, Fregion!!!
			getOwner().getController().addTask(TaskId.DESPAWN,
				ThreadPoolManager.getInstance().schedule(() -> getOwner().getController().deleteIfAliveOrCancelRespawn(), 2000));
		}, 30000);
	}

	@Override
	protected void handleDespawned() {
		if (idleTask != null && !idleTask.isDone())
			idleTask.cancel(true);
		super.handleDespawned();
	}
}
