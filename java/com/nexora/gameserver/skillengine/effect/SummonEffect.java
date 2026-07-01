package com.nexora.gameserver.skillengine.effect;

import java.util.concurrent.Future;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.Summon;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.summons.UnsummonType;
import com.nexora.gameserver.services.summons.SummonsService;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Simple
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonEffect")
public class SummonEffect extends EffectTemplate {

	@XmlAttribute(name = "npc_id", required = true)
	protected int npcId;
	@XmlAttribute(name = "time", required = true)
	protected int time; // in seconds

	@Override
	public void applyEffect(Effect effect) {
		Player effected = (Player) effect.getEffected();
		Summon summon = SummonsService.createSummon(effected, npcId, effect.getSkillId(), effect.getSkillLevel(), time);
		if (summon != null && time > 0) {
			Future<?> task = ThreadPoolManager.getInstance().schedule(() -> summon.getController().release(UnsummonType.UNSPECIFIED), time * 1000);
			summon.getController().addTask(TaskId.DESPAWN, task);
			effected.getEffectController().removePetOrderUnSummonEffects();
		}
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSuccessEffect(this);
	}
}
