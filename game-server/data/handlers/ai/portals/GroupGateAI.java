package ai.portals;

import static com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE.STR_SKILL_CAN_NOT_USE_GROUPGATE_NO_RIGHT;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.AIRequest;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer, nrg
 */
@AIName("groupgate")
public class GroupGateAI extends NpcAI {

	private static final int CANCEL_DIALOG_METERS = 9;

	public GroupGateAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		if (!player.getCommonData().isDaeva()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CAN_NOT_USE_GROUPGATE_BEFORE_CHANGE_CLASS());
			return;
		}

		if (!player.equals(getCreator()) && (!player.isInGroup() || !player.getPlayerGroup().hasMember(getCreatorId()))) {
			PacketSendUtility.sendPacket(player, STR_SKILL_CAN_NOT_USE_GROUPGATE_NO_RIGHT());
			return;
		}

		AIActions.addRequest(this, player, SM_QUESTION_WINDOW.STR_ASK_GROUP_GATE_DO_YOU_ACCEPT_MOVE, CANCEL_DIALOG_METERS, new AIRequest() {

			private boolean decisionTaken = false;

			@Override
			public void acceptRequest(Creature requester, Player responder, int requestId) {
				if (!decisionTaken) {
					switch (getNpcId()) {
						// Group Gates
						case 833208:
						case 749017:
							TeleportService.teleportTo(responder, 110010000, 1444.9f, 1577.2f, 572.9f, (byte) 0, TeleportAnimation.JUMP_IN);
							break;
						case 833207:
						case 749083:
							TeleportService.teleportTo(responder, 120010000, 1657.5f, 1398.7f, 194.7f, (byte) 0, TeleportAnimation.JUMP_IN);
							break;
						// Binding Group Gates
						case 749131:
						case 749132:
							TeleportService.moveToBindLocation(responder);
							break;
					}
					decisionTaken = true;
				}
			}

			@Override
			public void denyRequest(Creature requester, Player responder) {
				decisionTaken = true;
			}

		});
	}
}
