package ai.instance.dragonLordsRefuge;

import java.util.concurrent.Future;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.poll.AIQuestion;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.templates.item.ItemAttackType;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.GeneralNpcAI;

/**
 * @author Luzien, Estrayl
 */
@AIName("ultimate_atrocity")
public class UltimateAtrocityAI extends GeneralNpcAI {

	private Future<?> task;

	public UltimateAtrocityAI(Npc owner) {
		super(owner);
	}

	@Override
	public ItemAttackType modifyAttackType(ItemAttackType type) {
		return ItemAttackType.MAGICAL_FIRE;
	}

	@Override
	public float modifyOwnerDamage(float damage, Creature effected, Effect effect) {
		return damage / 4;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		int skill = switch (getNpcId()) {
			case 283244 -> 21160;
			case 283240 -> 21156;
			case 283237, 283241 -> 20923;
			default -> 0;
		};

		if (skill == 0)
			return;

		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> AIActions.useSkill(this, skill), 500, 2000);

		ThreadPoolManager.getInstance().schedule(() -> AIActions.deleteOwner(this), 11000);
	}

	@Override
	public void handleDespawned() {
		task.cancel(true);
		super.handleDespawned();
	}

	@Override
	public boolean ask(AIQuestion question) {
		return switch (question) {
			case ALLOW_DECAY, ALLOW_RESPAWN, REWARD_AP_XP_DP_LOOT -> false;
			default -> super.ask(question);
		};
	}
}
