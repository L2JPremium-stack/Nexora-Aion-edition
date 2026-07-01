package ai.quests;

import java.util.ArrayList;
import java.util.List;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.handler.CreatureEventHandler;
import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestActionType;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.services.QuestService;
import com.nexora.gameserver.services.drop.DropService;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI;

/**
 * @author xTz
 */
@AIName("quest_use_item")
public class QuestItemNpcAI extends ActionItemNpcAI {

	public QuestItemNpcAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleDialogStart(Player player) {
		if (!(QuestEngine.getInstance().onCanAct(new QuestEnv(getOwner(), player, 0), getObjectTemplate().getTemplateId(),
			QuestActionType.ACTION_ITEM_USE))) {
			return;
		}
		super.handleDialogStart(player);
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		QuestEnv env = new QuestEnv(getOwner(), player, 0, DialogAction.USE_OBJECT);
		if (!QuestEngine.getInstance().onDialog(env)) {
			if (getObjectTemplate().isDialogNpc()) // show default dialog
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
			return;
		}

		if (QuestService.getQuestDrop(getNpcId()).isEmpty())
			return;

		List<Player> registeredPlayers = new ArrayList<>();
		if (player.isInGroup()) {
			registeredPlayers = QuestService.getEachDropMembersGroup(player.getPlayerGroup(), getNpcId(), env.getQuestId());
			if (registeredPlayers.isEmpty()) {
				registeredPlayers.add(player);
			}
		} else if (player.isInAlliance()) {
			registeredPlayers = QuestService.getEachDropMembersAlliance(player.getPlayerAlliance(), getNpcId(), env.getQuestId());
			if (registeredPlayers.isEmpty()) {
				registeredPlayers.add(player);
			}
		} else {
			registeredPlayers.add(player);
		}
		AIActions.registerDrop(this, player, registeredPlayers);
		AIActions.die(this, player);
		DropService.getInstance().requestDropList(player, getObjectId());
	}

	@Override
	protected void handleCreatureSee(Creature creature) {
		CreatureEventHandler.onCreatureSee(this, creature);
	}
}
