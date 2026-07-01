package com.nexora.gameserver.ai;

import java.util.EnumSet;

import com.nexora.gameserver.ai.handler.*;
import com.nexora.gameserver.ai.manager.SimpleAttackManager;
import com.nexora.gameserver.ai.manager.WalkManager;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.configs.main.AIConfig;
import com.nexora.gameserver.configs.main.SiegeConfig;
import com.nexora.gameserver.controllers.attack.AggroList;
import com.nexora.gameserver.controllers.effect.EffectController;
import com.nexora.gameserver.controllers.movement.NpcMoveController;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.TribeClass;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.siege.SiegeNpc;
import com.nexora.gameserver.model.skill.NpcSkillList;
import com.nexora.gameserver.model.stats.container.NpcLifeStats;
import com.nexora.gameserver.model.templates.npc.NpcTemplate;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.services.NpcShoutsService;
import com.nexora.gameserver.services.SiegeService;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.world.WorldType;
import com.nexora.gameserver.world.knownlist.KnownList;

/**
 * @author ATracer
 */
public abstract class NpcAI extends AITemplate<Npc> {

	private static final EnumSet<Race> apRewardingRaces = EnumSet.of(Race.ASMODIANS, Race.DARK, Race.DRAGON, Race.DRAGONET, Race.DRAKAN, Race.ELYOS,
		Race.GCHIEF_DARK, Race.GCHIEF_DRAGON, Race.GCHIEF_LIGHT, Race.GHENCHMAN_DARK, Race.GHENCHMAN_LIGHT, Race.LIGHT, Race.LIZARDMAN, Race.NAGA,
		Race.SIEGEDRAKAN);

	public NpcAI(Npc owner) {
		super(owner);
	}

	protected NpcTemplate getObjectTemplate() {
		return getOwner().getObjectTemplate();
	}

	protected SpawnTemplate getSpawnTemplate() {
		return getOwner().getSpawn();
	}

	protected NpcLifeStats getLifeStats() {
		return getOwner().getLifeStats();
	}

	protected Race getRace() {
		return getOwner().getRace();
	}

	protected TribeClass getTribe() {
		return getOwner().getTribe();
	}

	protected EffectController getEffectController() {
		return getOwner().getEffectController();
	}

	protected KnownList getKnownList() {
		return getOwner().getKnownList();
	}

	protected AggroList getAggroList() {
		return getOwner().getAggroList();
	}

	protected NpcSkillList getSkillList() {
		return getOwner().getSkillList();
	}

	protected VisibleObject getCreator() {
		return getOwner().getCreator();
	}

	/**
	 * DEPRECATED as movements will be processed as commands only from ai
	 */
	protected NpcMoveController getMoveController() {
		return getOwner().getMoveController();
	}

	protected int getNpcId() {
		return getOwner().getNpcId();
	}

	protected int getCreatorId() {
		return getOwner().getCreatorId();
	}

	protected boolean isInRange(VisibleObject object, int range) {
		return PositionUtil.isInRange(getOwner(), object, range);
	}

	@Override
	protected void handleActivate() {
		ActivateEventHandler.onActivate(this);
	}

	@Override
	protected void handleDeactivate() {
		if (SiegeConfig.BALAUR_AUTO_ASSAULT && getOwner() instanceof SiegeNpc || getOwner().isRaidMonster())
			return;
		ActivateEventHandler.onDeactivate(this);
	}

	@Override
	protected void handleBeforeSpawned() {
		SpawnEventHandler.onBeforeSpawn(this);
	}

	@Override
	protected void handleSpawned() {
		SpawnEventHandler.onSpawn(this);
		ShoutEventHandler.onSpawn(this);
	}

	@Override
	protected void handleDespawned() {
		ShoutEventHandler.onBeforeDespawn(this);
		SpawnEventHandler.onDespawn(this);
	}

	@Override
	protected void handleDied() {
		DiedEventHandler.onDie(this);
	}

	@Override
	protected void handleMoveArrived() {
		ShoutEventHandler.onReachedWalkPoint(this);
	}

	@Override
	protected void handleTargetChanged(Creature creature) {
		ShoutEventHandler.onSwitchedTarget(this, creature);
	}

	@Override
	public boolean ask(AIQuestion question) {
		return switch (question) {
			case CAN_SHOUT -> AIConfig.SHOUTS_ENABLE && NpcShoutsService.getInstance().mayShout(getOwner());
			case ALLOW_RESPAWN -> SiegeService.getInstance().isRespawnAllowed(getOwner());
			case ALLOW_DECAY, REWARD_AP_XP_DP_LOOT, REWARD_LOOT -> true;
			case IS_IMMUNE_TO_ABNORMAL_STATES -> getOwner().isBoss() || getOwner().hasStatic();
			case REWARD_AP -> {
				WorldType wt = getOwner().getWorldType();
				yield wt == WorldType.ABYSS || wt != WorldType.ELYSEA && wt != WorldType.ASMODAE && apRewardingRaces.contains(getRace());
			}
			case REMOVE_EFFECTS_ON_MAP_REGION_DEACTIVATE -> !getOwner().isInInstance();
			default -> false;
		};
	}

	@Override
	public boolean isDestinationReached() {
		return switch (getState()) {
			case CONFUSE, FEAR -> PositionUtil.isInRange(getOwner(), getOwner().getMoveController().getTargetX2(),
				getOwner().getMoveController().getTargetY2(), getOwner().getMoveController().getTargetZ2(), 1);
			case FIGHT -> SimpleAttackManager.isTargetInAttackRange(getOwner());
			case RETURNING -> {
				SpawnTemplate spawn = getOwner().getSpawn();
				yield PositionUtil.isInRange(getOwner(), spawn.getX(), spawn.getY(), spawn.getZ(), 1);
			}
			case FOLLOWING -> FollowEventHandler.isInRange(this, getOwner().getTarget());
			case WALKING, FORCED_WALKING -> getSubState() == AISubState.TALK || WalkManager.isArrivedAtPoint(this);
			default -> true;
		};
	}

	@Override
	protected void handleMoveValidate() {
		MoveEventHandler.onMoveValidate(this);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		CreatureEventHandler.onCreatureMoved(this, creature);
	}

	public boolean isMoveSupported() {
		return getOwner().getGameStats().getMovementSpeed().getCurrent() > 0 && !isInSubState(AISubState.FREEZE);
	}

	/**
	 * NCsoft uses different non-visible npcs as a sensor to trigger different events
	 */
	public void handleCreatureDetected(Creature creature) {

	}
}
