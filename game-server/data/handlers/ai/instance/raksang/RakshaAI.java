package ai.instance.raksang;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.HpPhases;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI;

/**
 * @author xTz
 */
@AIName("raksha")
public class RakshaAI extends AggressiveNpcAI implements HpPhases.PhaseHandler {

	private final HpPhases hpPhases = new HpPhases(75);
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	private Future<?> phaseTask;

	public RakshaAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDespawned() {
		cancelPhaseTask();
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		cancelPhaseTask();
		spawn(730445, 1062.281f, 889.900f, 138.744f, (byte) 29);
		super.handleDied();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			PacketSendUtility.broadcastMessage(getOwner(), 1401152);
		}
		hpPhases.tryEnterNextPhase(this);
	}

	@Override
	public void handleHpPhase(int phaseHpPercent) {
		PacketSendUtility.broadcastMessage(getOwner(), 1401154);
		startPhaseTask();
	}

	private void startPhaseTask() {
		phaseTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isDead()) {
					cancelPhaseTask();
				} else {
					SkillEngine.getInstance().getSkill(getOwner(), 19938, 46, getOwner()).useNoAnimationSkill();
					List<Player> players = getLifedPlayers();
					if (!players.isEmpty()) {
						int size = players.size();
						if (players.size() < 4) {
							for (Player p : players) {
								spawnRubble(p);
							}
						} else {
							int count = Rnd.get(3, size);
							for (int i = 0; i < count; i++) {
								spawnRubble(Rnd.get(players));
							}
						}
					}
				}
			}

		}, 3000, 15000);
	}

	private void spawnRubble(Player player) {
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		if (x > 0 && y > 0 && z > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (!isDead()) {
						spawn(282325, x, y, z, (byte) 0);
					}
				}

			}, 3000);

		}
	}

	private List<Player> getLifedPlayers() {
		List<Player> players = new ArrayList<>();
		getKnownList().forEachPlayer(player -> {
			if (!player.isDead()) {
				players.add(player);
			}
		});
		return players;
	}

	private void cancelPhaseTask() {
		if (phaseTask != null && !phaseTask.isDone()) {
			phaseTask.cancel(true);
		}
	}

	@Override
	protected void handleBackHome() {
		cancelPhaseTask();
		isAggred.set(false);
		super.handleBackHome();
		hpPhases.reset();
	}

}
