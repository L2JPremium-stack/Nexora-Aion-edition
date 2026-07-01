package com.nexora.gameserver.ai.handler;

import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.ai.AISubState;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.ai.event.AIEventType;
import com.nexora.gameserver.ai.manager.AttackManager;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureVisualState;
import com.nexora.gameserver.model.templates.npc.NpcTemplateType;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.services.TribeRelationService;
import com.nexora.gameserver.skillengine.effect.AbnormalState;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.world.geo.GeoService;

/**
 * @author ATracer
 */
public class CreatureEventHandler {

	/**
	 * @param npcAI
	 * @param creature
	 */
	public static void onCreatureMoved(NpcAI npcAI, Creature creature) {
		checkAggro(npcAI, creature);
		if (creature instanceof Player) {
			Player player = (Player) creature;
			QuestEngine.getInstance().onAtDistance(new QuestEnv(npcAI.getOwner(), player, 0));
		}
	}

	/**
	 * @param npcAI
	 * @param creature
	 */
	public static void onCreatureSee(NpcAI npcAI, Creature creature) {
		if (npcAI.isInSubState(AISubState.TARGET_LOST) && creature.equals(npcAI.getTarget())) { // see target again after hide end
			npcAI.setSubStateIfNot(AISubState.NONE);
			if (npcAI.isInState(AIState.FIGHT)) { // continue to attack
				AttackManager.scheduleNextAttack(npcAI);
				return;
			}
		}
		checkAggro(npcAI, creature);
		if (creature instanceof Player) {
			Player player = (Player) creature;
			QuestEngine.getInstance().onAtDistance(new QuestEnv(npcAI.getOwner(), player, 0));
		}
	}

	protected static void checkAggro(NpcAI ai, Creature creature) {
		if (ai.isInState(AIState.FIGHT))
			return;

		if (ai.isInState(AIState.RETURNING))
			return;

		if (creature.isDead())
			return;

		if (creature.isInVisualState(CreatureVisualState.BLINKING))
			return;

		if (creature.isFlag())
			return;

		Npc owner = ai.getOwner();
		if (!owner.isSpawned())
			return;

		if (!owner.canSee(creature))
			return;

		if (owner.getEffectController().isAbnormalSet(AbnormalState.SANCTUARY))
			return;

		if (!owner.getPosition().isMapRegionActive())
			return;

		if (isInSeeRange(creature, owner)) {
			ai.handleCreatureDetected(creature); // TODO: Move to AIEventType, prevent calling multiple times
			if (TribeRelationService.isAggressive(owner, creature) && !TribeRelationService.isFriend(owner, creature) && creature.isEnemyFrom(owner)) {
				if (validateAggro(owner, creature) && GeoService.getInstance().canSee(owner, creature)) {
					ShoutEventHandler.onSee(ai, creature);
					if (ai.canThink())
						ai.onCreatureEvent(AIEventType.CREATURE_AGGRO, creature);
				}
			} else { // non aggressive (should we consider also using geo canSee checks here?)
				ShoutEventHandler.onSee(ai, creature);
			}
		}
	}

	private static boolean isInSeeRange(Creature creature, Npc npc) {
		if (npc.getAggroAngle() == 0 || npc.getAggroRange() == 0)
			return false;
		if (!PositionUtil.isInRange(npc, creature, npc.getAggroRange(), false))
			return false;
		return PositionUtil.isInFrontOf(creature, npc, npc.getAggroAngle() / 2f) || PositionUtil.isInRange(npc, creature, npc.getShortAggroRange(), false);
	}

	private static boolean validateAggro(Npc owner, Creature creature) {
		return creature.getLevel() - owner.getLevel() < 10 || owner.getObjectTemplate().getNpcTemplateType() == NpcTemplateType.GUARD
			|| owner.getObjectTemplate().getNpcTemplateType() == NpcTemplateType.ABYSS_GUARD;
	}
}
