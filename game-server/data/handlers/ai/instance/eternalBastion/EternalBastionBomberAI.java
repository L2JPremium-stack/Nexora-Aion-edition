package ai.instance.eternalBastion;

import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.manager.WalkManager;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.skillengine.model.SkillTemplate;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Cheatkiller, Estrayl
 */
@AIName("eternal_bastion_bomber")
public class EternalBastionBomberAI extends EternalBastionAggressiveNpcAI {

	private AtomicBoolean isExplosionActivated = new AtomicBoolean();

	public EternalBastionBomberAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(this::startWalking, 3000);
	}

	private void startWalking() {
		WalkManager.startWalking(this);
		getOwner().unsetState(CreatureState.WALK_MODE);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isExplosionActivated.compareAndSet(false, true))
			SkillEngine.getInstance().getSkill(getOwner(), 21143, 1, getOwner()).useSkill();
	}

	@Override
	public void onStartUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		if (skillTemplate.getSkillId() == 21143)
			isExplosionActivated.set(true);
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		if (skillTemplate.getSkillId() == 21143) {
			ThreadPoolManager.getInstance().schedule(() -> SkillEngine.getInstance().getSkill(getOwner(), 20549, 1, getOwner()).useSkill(), 750); // Self-Destruction
			spawn(284707, getPosition().getX(), getPosition().getY(), getPosition().getZ(), (byte) 0); // deals ~500k damage to gates
			spawn(284707, getPosition().getX(), getPosition().getY(), getPosition().getZ(), (byte) 0); // not 100% sure about this second one
		}
	}
}
