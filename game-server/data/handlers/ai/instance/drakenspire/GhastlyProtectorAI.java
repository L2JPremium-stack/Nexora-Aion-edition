package ai.instance.drakenspire;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.controllers.attack.AggroTarget;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.templates.item.ItemAttackType;
import com.nexora.gameserver.skillengine.model.SkillTemplate;
import com.nexora.gameserver.utils.PositionUtil;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNoLootNpcAI;

/**
 * @author Estrayl
 */
@AIName("drakenspire_ghastly_protector")
public class GhastlyProtectorAI extends AggressiveNoLootNpcAI {

	public GhastlyProtectorAI(Npc owner) {
		super(owner);
	}

	@Override
	public ItemAttackType modifyAttackType(ItemAttackType type) {
		return ItemAttackType.MAGICAL_WIND;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(this::aggroPlayer, 1000);
		getOwner().getGameStats().setNextSkillDelay(0);
	}

	private void aggroPlayer() {
		getKnownList().streamPlayers()
			.filter(p -> !p.isDead() && PositionUtil.isInRange(p, 152.38f, 518.68f, 1749.6f, 24))
			.findAny()
			.ifPresent(p -> getAggroList().addHate(p, 10000));
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		if (skillTemplate.getSkillId() == 21883)
			addHateToRandomTarget();
	}

	private void addHateToRandomTarget() {
		getAggroList().addHate(getAggroList().getTarget(AggroTarget.RANDOM), 10000);
	}

}
