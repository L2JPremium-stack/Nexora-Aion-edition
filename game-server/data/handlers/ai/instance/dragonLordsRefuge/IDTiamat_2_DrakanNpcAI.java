package ai.instance.dragonLordsRefuge;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.OneDmgAI;

/**
 * @author Estrayl March 10th, 2018
 */
@AIName("IDTiamat_2_Drakan_NPC")
public class IDTiamat_2_DrakanNpcAI extends OneDmgAI {

	public IDTiamat_2_DrakanNpcAI(Npc owner) {
		super(owner);
	}

	@Override
	public float modifyDamage(Creature attacker, float damage, Effect effect) {
		return damage;
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		AIActions.targetCreature(this, Rnd.get(getPosition().getWorldMapInstance().getPlayersInside()));
		setStateIfNot(AIState.WALKING);
		getOwner().setState(CreatureState.ACTIVE, true);
		getMoveController().moveToTargetObject();
		PacketSendUtility.broadcastToMap(getOwner(), new SM_EMOTION(getOwner(), EmotionType.WALK));
	}

}
