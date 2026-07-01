package com.nexora.gameserver.ai;

import com.nexora.gameserver.ai.event.AIEventType;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.model.animations.AttackHandAnimation;
import com.nexora.gameserver.model.animations.AttackTypeAnimation;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.templates.item.ItemAttackType;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.SkillTemplate;

/**
 * @author ATracer
 */
public interface AI {

	void onCreatureEvent(AIEventType event, Creature creature);

	void onCustomEvent(int eventId, Object... args);

	void onGeneralEvent(AIEventType event);

	/**
	 * If already handled dialog return true.
	 */
	boolean onDialogSelect(Player player, int dialogActionId, int questId, int extendedRewardIndex);

	void think();

	boolean canThink();

	AIState getState();

	AISubState getSubState();

	String getName();

	/**
	 * Ask AI instance for the answer to the specified question.
	 * 
	 * @param question
	 * @return The answer, true or false.
	 */
	boolean ask(AIQuestion question);

	boolean isLogging();

	/**
	 * @param attacker
	 * @param damage
	 *          - The calculated damage from given attacker
	 * @param effect
	 *          - The effect which caused the damage (may be null)
	 * @return The effectively received damage
	 */
	float modifyDamage(Creature attacker, float damage, Effect effect);

	/**
	 * @param damage
	 *          - The calculated damage output of this creature
	 * @param effected
	 * @param effect
	 * @return The effective damage output of this creature
	 */
	float modifyOwnerDamage(float damage, Creature effected, Effect effect);

	/**
	 * Used to manipulate any game stat of the owner.
	 *
	 * @param stat
	 */
	void modifyOwnerStat(Stat2 stat);

	ItemAttackType modifyAttackType(ItemAttackType type);

	int modifyAggroRange(int value);

	int modifyAggroAngle(int value);

	void onStartUseSkill(SkillTemplate skillTemplate, int skillLevel);

	void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel);

	void onEffectApplied(Effect effect);

	void onEffectEnd(Effect effect);

	AttackHandAnimation modifyAttackHandAnimation(AttackHandAnimation attackHandAnimation);

	AttackTypeAnimation getAttackTypeAnimation(Creature target);

	int modifyInitialSkillDelay(int delay);

	boolean isDestinationReached();
}
