package ai.instance.rentusBase;

import java.util.concurrent.atomic.AtomicBoolean;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.manager.WalkManager;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.state.CreatureState;
import com.nexora.gameserver.model.templates.walker.RouteStep;
import com.nexora.gameserver.model.templates.walker.WalkerTemplate;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.PositionUtil;

import ai.GeneralNpcAI;

/**
 * @author xTz
 */
@AIName("imprisoned_reian")
public class ImprisonedReianAI extends GeneralNpcAI {

	private AtomicBoolean isSaved = new AtomicBoolean(false);
	private AtomicBoolean isAsked = new AtomicBoolean(false);
	private String walkerId;
	private WalkerTemplate template;

	public ImprisonedReianAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		walkerId = getSpawnTemplate().getWalkerId();
		getSpawnTemplate().setWalkerId(null);
		if (walkerId != null) {
			template = DataManager.WALKER_DATA.getWalkerTemplate(walkerId);
		}
		super.handleSpawned();
	}

	@Override
	protected void handleMoveArrived() {
		RouteStep step = getOwner().getMoveController().getCurrentStep();
		super.handleMoveArrived();
		if (template.getRouteSteps().size() - 4 == step.getStepIndex()) {
			getSpawnTemplate().setWalkerId(null);
			WalkManager.stopWalking(this);
			AIActions.deleteOwner(this);
		}
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (walkerId != null) {
			if (creature instanceof Player) {
				final Player player = (Player) creature;
				if (PositionUtil.getDistance(getOwner(), player) <= 21) {
					if (isAsked.compareAndSet(false, true)) {
						switch (Rnd.get(1, 10)) {
							case 1:
								PacketSendUtility.broadcastMessage(getOwner(), 390563);
								break;
							case 2:
								PacketSendUtility.broadcastMessage(getOwner(), 390567);
								break;
						}
					}
				}
				if (PositionUtil.getDistance(getOwner(), player) <= 6) {
					if (isSaved.compareAndSet(false, true)) {
						getSpawnTemplate().setWalkerId(walkerId);
						WalkManager.startWalking(this);
						getOwner().setState(CreatureState.ACTIVE, true);
						PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.CHANGE_SPEED, 0, getObjectId()));
						switch (Rnd.get(1, 10)) {
							case 1:
								PacketSendUtility.broadcastMessage(getOwner(), 342410);
								break;
							case 2:
								PacketSendUtility.broadcastMessage(getOwner(), 342411);
								break;
						}
					}
				}
			}
		}
	}
}
