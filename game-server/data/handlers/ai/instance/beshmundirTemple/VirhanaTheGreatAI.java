package ai.instance.beshmundirTemple;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI;

/**
 * @author Luzien
 */
@AIName("virhana")
public class VirhanaTheGreatAI extends AggressiveNpcAI {

	private boolean isStart;
	private int count;

	public VirhanaTheGreatAI(Npc owner) {
		super(owner);
	}

	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (!isStart) {
			isStart = true;
			scheduleRage();
		}
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		isStart = false;
	}

	private void scheduleRage() {
		if (isDead() || !isStart) {
			return;
		}
		AIActions.useSkill(this, 19121);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				startRage();
			}

		}, 70000);
	}

	private void startRage() {
		if (isDead() || !isStart) {
			return;
		}
		if (count < 12) {
			AIActions.useSkill(this, 18897);
			count++;

			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					startRage();
				}

			}, 10000);
		} else { // restart after a douzen casts
			count = 0;
			scheduleRage();
		}
	}
}
