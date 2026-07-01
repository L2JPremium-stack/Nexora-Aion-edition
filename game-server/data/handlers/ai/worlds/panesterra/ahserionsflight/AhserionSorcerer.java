package ai.worlds.panesterra.ahserionsflight;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.templates.item.ItemAttackType;

/**
 * @author Estrayl
 */
@AIName("ahserion_sorcerer")
public class AhserionSorcerer extends AhserionAggressiveNpcAI {

	public AhserionSorcerer(Npc owner) {
		super(owner);
	}

	@Override
	public ItemAttackType modifyAttackType(ItemAttackType type) {
		return ItemAttackType.MAGICAL_FIRE;
	}
}
