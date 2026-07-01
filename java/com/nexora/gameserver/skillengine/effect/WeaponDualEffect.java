package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.skill.PlayerSkillEntry;
import com.nexora.gameserver.skillengine.model.Effect;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WeaponDualEffect")
public class WeaponDualEffect extends BufEffect {

	@Override
	public void startEffect(Effect effect) {
		if (effect.getEffected() instanceof Player p) {
			p.getGameStats().setSkillEfficiency(skillEfficiency / 100f);
			p.getGameStats().setMaxDamageChance(maxDamageChance + effect.getSkillLevel() * maxDamageDelta);
			p.getGameStats().setMinDamageRatio((value + effect.getSkillLevel() * delta) / 100f);
			p.getGameStats().updateStatsVisually();
		}
	}

	@Override
	public void endEffect(Effect effect) {
		if (effect.getEffected() instanceof Player p) {
			p.getGameStats().setSkillEfficiency(0);
			p.getGameStats().setMaxDamageChance(0);
			p.getGameStats().setMinDamageRatio(0);
			p.getGameStats().updateStatsVisually();
		}
		super.endEffect(effect);
	}

	public static boolean hasDualWieldEffect(Player player) {
		if (!player.isSpawned()) { // fallback for enterWorld
			for (PlayerSkillEntry skillEntry : player.getSkillList().getAllSkills()) {
				Effects effects = DataManager.SKILL_DATA.getSkillTemplate(skillEntry.getSkillId()).getEffects();
				if (effects != null && effects.hasAnyEffectType(EffectType.WEAPONDUAL))
					return true;
			}
		}
		return player.getGameStats().getSkillEfficiency() != 0;
	}
}
