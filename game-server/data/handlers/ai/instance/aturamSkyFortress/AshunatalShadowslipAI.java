package ai.instance.aturamSkyFortress;

import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.manager.WalkManager;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.skillengine.model.SkillTemplate;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI;

/**
 * @author xTz
 */
@AIName("ashunatal_shadowslip")
public class AshunatalShadowslipAI extends AggressiveNpcAI {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	private boolean canThink = true;

	public AshunatalShadowslipAI(Npc owner) {
		super(owner);
	}

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	protected void handleCreatureAggro(Creature creature) {
		super.handleCreatureAggro(creature);
		if (isHome.compareAndSet(true, false)) {
			getPosition().getWorldMapInstance().setDoorState(2, true); // this actually closes it on client side (wtf)
		}
	}

	@Override
	protected void handleBackHome() {
		isHome.set(true);
		super.handleBackHome();
		getPosition().getWorldMapInstance().setDoorState(2, false); // this actually opens it on client side (wtf)
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		// ashunatal got killed early, therefore she could not spawn her shadow which usually would open the door
		getPosition().getWorldMapInstance().setDoorState(17, false); // this actually opens it on client side (wtf)
		getPosition().getWorldMapInstance().setDoorState(2, false); // this actually opens it on client side (wtf)
	}

	@Override
	public void onEndUseSkill(SkillTemplate skillTemplate, int skillLevel) {
		super.onEndUseSkill(skillTemplate, skillLevel);
		if (skillTemplate.getSkillId() == 19417) {
			canThink = false;
			ThreadPoolManager.getInstance().schedule(() -> {
				getSpawnTemplate().setWalkerId("3002400001");
				getOwner().setState(CreatureState.ACTIVE, true);
				WalkManager.startWalking(this);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(() -> getOwner().getController().delete(), 4000);
			}, 3000);
		}
	}

}
