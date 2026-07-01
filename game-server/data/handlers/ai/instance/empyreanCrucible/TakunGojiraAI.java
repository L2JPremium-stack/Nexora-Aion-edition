package ai.instance.empyreanCrucible;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI;

/**
 * @author Luzien
 */
@AIName("takun_gojira")
public class TakunGojiraAI extends AggressiveNpcAI {

	private Npc counterpart;

	public TakunGojiraAI(Npc owner) {
		super(owner);
	}

	@Override
	public void handleSpawned() {
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				counterpart = getPosition().getWorldMapInstance().getNpc(getNpcId() == 217596 ? 217597 : 217596);
				if (counterpart != null)
					getAggroList().addHate(counterpart, 1000000);
			}
		}, 500);
	}

}
