package ai.instance.rentusBase;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.ai.event.AIEventType;
import com.nexora.gameserver.ai.manager.EmoteManager;
import com.nexora.gameserver.ai.manager.WalkManager;
import com.nexora.gameserver.controllers.attack.AggroTarget;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.world.WorldMapInstance;

import ai.AggressiveNpcAI;

/**
 * @author xTz
 */
@AIName("captain_xasta")
public class CaptainXastaAI extends AggressiveNpcAI {

	private boolean canThink = true;
	private Future<?> phaseTask;
	private AtomicBoolean isHome = new AtomicBoolean(true);

	public CaptainXastaAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			if (getNpcId() == 217309) {
				PacketSendUtility.broadcastMessage(getOwner(), 1500388);
				startPhaseTask(this);
			} else {
				startPhase2Task();
			}
		}
	}

	private void cancelPhaseTask() {
		if (phaseTask != null && !phaseTask.isDone()) {
			phaseTask.cancel(true);
		}
	}

	private void startPhaseTask(final NpcAI ai) {
		phaseTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isDead()) {
					cancelPhaseTask();
				} else {
					canThink = false;
					EmoteManager.emoteStopAttacking(getOwner());
					getSpawnTemplate().setWalkerId("B186C8F43FF13FDD50FA9483B7D8C2BEABAE7F5C");
					WalkManager.startWalking(ai);
					startRun(getOwner());
					spawnHelpers(ai);
					startSanctuaryEvent();
				}
			}
		}, 28000, 28000);
	}

	@Override
	protected void handleMoveArrived() {
		super.handleMoveArrived();
		if (getSpawnTemplate().getWalkerId() != null) {
			getSpawnTemplate().setWalkerId(null);
			WalkManager.stopWalking(this);
		}
	}

	private void startPhase2Task() {
		phaseTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isDead()) {
					cancelPhaseTask();
				} else {
					SkillEngine.getInstance().getSkill(getOwner(), 19729, 60, getOwner()).useNoAnimationSkill();
					PacketSendUtility.broadcastMessage(getOwner(), 1500392);
				}
			}
		}, 30000, 30000);
	}

	private void startSanctuaryEvent() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isDead()) {
					canThink = true;
					Creature creature = getAggroList().getTarget(AggroTarget.MOST_HATED);
					if (creature == null) {
						setStateIfNot(AIState.FIGHT);
						getMoveController().abortMove();
						onGeneralEvent(AIEventType.ATTACK_FINISH);
						onGeneralEvent(AIEventType.BACK_HOME);
					} else {
						getMoveController().abortMove();
						getOwner().setTarget(creature);
						getOwner().getGameStats().renewLastAttackTime();
						getOwner().getGameStats().renewLastAttackedTime();
						getOwner().getGameStats().renewLastSkillTime();
						setStateIfNot(AIState.FIGHT);
						handleMoveValidate();
						SkillEngine.getInstance().getSkill(getOwner(), 19657, 60, getOwner()).useNoAnimationSkill();
					}
				}
			}

		}, 23000);
	}

	private void startRun(Npc npc) {
		npc.setState(CreatureState.ACTIVE, true);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.CHANGE_SPEED, 0, npc.getObjectId()));
	}

	private void spawnHelpers(final NpcAI ai) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isDead()) {
					PacketSendUtility.broadcastMessage(getOwner(), 1500389);
					SkillEngine.getInstance().getSkill(getOwner(), 19968, 60, getOwner()).useNoAnimationSkill();
					Npc npc1 = (Npc) spawn(282604, 263f, 537f, 203f, (byte) 0);
					Npc npc2 = (Npc) spawn(282604, 186f, 555f, 203f, (byte) 0);
					npc1.getSpawn().setWalkerId("30028000014");
					WalkManager.startWalking((NpcAI) npc1.getAi());
					npc2.getSpawn().setWalkerId("30028000015");
					WalkManager.startWalking((NpcAI) npc2.getAi());
					startRun(npc1);
					startRun(npc2);
				}
			}

		}, 3000);
	}

	private void deleteHelpers() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		if (instance != null) {
			deleteNpcs(instance.getNpcs(282604));
		}
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().delete();
			}
		}
	}

	@Override
	protected void handleDied() {
		cancelPhaseTask();
		if (getNpcId() == 217309) {
			PacketSendUtility.broadcastMessage(getOwner(), 1500390);
			spawn(217310, 238.160f, 598.624f, 178.480f, (byte) 0);
			deleteHelpers();
			AIActions.deleteOwner(this);
		} else {
			PacketSendUtility.broadcastMessage(getOwner(), 1500391);
			final WorldMapInstance instance = getPosition().getWorldMapInstance();
			if (instance != null) {
				final Npc ariana = instance.getNpc(799668);
				if (ariana != null) {
					ariana.getEffectController().removeEffect(19921);
					ThreadPoolManager.getInstance().schedule(() -> {
						ariana.getSpawn().setWalkerId("30028000016");
						WalkManager.startWalking((NpcAI) ariana.getAi());
					}, 1000);
					PacketSendUtility.broadcastMessage(ariana, 1500415, 4000);
					PacketSendUtility.broadcastMessage(ariana, 1500416, 13000);
					ThreadPoolManager.getInstance().schedule(() -> {
						SkillEngine.getInstance().getSkill(ariana, 19358, 60, ariana).useNoAnimationSkill();
						instance.setDoorState(145, true);
						deleteNpcs(instance.getNpcs(701156));
						ThreadPoolManager.getInstance().schedule(() -> ariana.getController().deleteIfAliveOrCancelRespawn(), 13000);
					}, 13000);
				}
			}
		}
		super.handleDied();
	}

	@Override
	protected void handleDespawned() {
		cancelPhaseTask();
		super.handleDespawned();
	}

	@Override
	protected void handleBackHome() {
		canThink = true;
		cancelPhaseTask();
		deleteHelpers();
		isHome.set(true);
		super.handleBackHome();
	}
}
