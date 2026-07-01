package ai.instance.tiamatStrongHold;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Cheatkiller
 */
@AIName("sinkingsand")
public class SinkingSandAI extends NpcAI {

	public SinkingSandAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		useskill();
	}

	private void useskill() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AIActions.useSkill(SinkingSandAI.this, 20723);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						AIActions.deleteOwner(SinkingSandAI.this);
					}
				}, 1000);
			}
		}, 3000);
	}
}
