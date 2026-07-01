package ai.instance.eternalBastion;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.controllers.attack.AggroTarget;
import com.nexora.gameserver.model.animations.AttackHandAnimation;
import com.nexora.gameserver.model.animations.AttackTypeAnimation;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.templates.item.ItemAttackType;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.SkillTemplate;
import com.nexora.gameserver.utils.PositionUtil;

/**
 * @author Estrayl
 */
@AIName("eternal_bastion_dragon")
public class EternalBastionDragonAI extends EternalBastionAggressiveNpcAI {

	private double dist;

	public EternalBastionDragonAI(Npc owner) {
		super(owner);
	}

	@Override
	public AttackTypeAnimation getAttackTypeAnimation(Creature target) {
		dist = PositionUtil.getDistance(getOwner(), target) - getObjectTemplate().getBoundRadius().getMaxOfFrontAndSide()
			- target.getObjectTemplate().getBoundRadius().getMaxOfFrontAndSide();
		return dist > 6 ? AttackTypeAnimation.RANGED : AttackTypeAnimation.MELEE;
	}

	@Override
	public float modifyOwnerDamage(float damage, Creature effected, Effect effect) {
		return effect == null && dist > 6 ? damage * 1.2f : damage;
	}

	@Override
	public ItemAttackType modifyAttackType(ItemAttackType type) {
		return dist > 6 ? ItemAttackType.MAGICAL_EARTH : ItemAttackType.PHYSICAL;
	}

	@Override
	public AttackHandAnimation modifyAttackHandAnimation(AttackHandAnimation attackHandAnimation) {
		return Rnd.get(AttackHandAnimation.values());
	}

	@Override
	public boolean ask(AIQuestion question) {
		if (question == AIQuestion.IS_IMMUNE_TO_ABNORMAL_STATES)
			return true;
		return super.ask(question);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		spawn(284697, getPosition().getX(), getPosition().getY(), getPosition().getZ(), (byte) 0);
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		if (skillTemplate.getSkillId() == 21239) // Gnaw
			getAggroList().addHate(getAggroList().getTarget(AggroTarget.RANDOM), 50000);
	}
}
