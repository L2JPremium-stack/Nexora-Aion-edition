package ai.worlds.panesterra.ahserionsflight;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.manager.EmoteManager;
import com.nexora.gameserver.configs.main.SiegeConfig;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.model.templates.item.ItemAttackType;
import com.nexora.gameserver.skillengine.model.Effect;

import ai.AggressiveNoLootNpcAI;

/**
 * @author Estrayl
 */
@AIName("camp_defense_cannon")
public class CampDefenseCannonAI extends AggressiveNoLootNpcAI {

	public CampDefenseCannonAI(Npc owner) {
		super(owner);
	}

	@Override
	public void modifyOwnerStat(Stat2 stat) {
		if (stat.getStat() == StatEnum.MAXHP)
			stat.setBaseRate(SiegeConfig.AHSERION_MAX_PLAYERS_PER_TEAM / 100f);
	}

	@Override
	public float modifyDamage(Creature attacker, float damage, Effect effect) {
		if (attacker instanceof Npc && effect != null) {
			switch (effect.getSkillId()) {
				case 21755: // Bombarding targets.
				case 21578: // Shield Penetration
				case 21583: // Artillery Blast
				case 21584: // Area Bombardment
					return damage * (SiegeConfig.AHSERION_MAX_PLAYERS_PER_TEAM / 100f);
			}
		}
		return super.modifyDamage(attacker, damage, effect);
	}

	@Override
	public ItemAttackType modifyAttackType(ItemAttackType type) {
		return ItemAttackType.MAGICAL_FIRE;
	}

	@Override
	public void handleFinishAttack() {
		if (!canThink())
			return;
		Npc npc = getOwner();
		EmoteManager.emoteStopAttacking(npc);
		npc.getController().loseAggro(false);
		npc.setSkillNumber(0);
	}
}
