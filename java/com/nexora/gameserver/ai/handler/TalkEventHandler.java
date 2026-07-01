package com.nexora.gameserver.ai.handler;

import com.nexora.gameserver.ai.AIState;
import com.nexora.gameserver.ai.AISubState;
import com.nexora.gameserver.ai.NpcAI;
import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.DialogPage;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.services.TownService;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class TalkEventHandler {

	public static void onTalk(NpcAI npcAI, Creature creature) {
		onSimpleTalk(npcAI, creature);

		if (creature instanceof Player player) {
			if (QuestEngine.getInstance().onDialog(new QuestEnv(npcAI.getOwner(), player, 0, DialogAction.USE_OBJECT)))
				return;
			// only player villagers can use villager npcs in oriel/pernon
			switch (npcAI.getOwner().getObjectTemplate().getTitleId()) {
				case 462877:
					int playerTownId = TownService.getInstance().getTownResidence(player);
					int currentTownId = TownService.getInstance().getTownIdByPosition(player);
					if (playerTownId != currentTownId) {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcAI.getOwner().getObjectId(), 44));
					} else {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcAI.getOwner().getObjectId(), 10));
					}
					return;
				default:
					int dialogPageId = DialogPage.getStartPageId(npcAI.getOwner(), player);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcAI.getOwner().getObjectId(), dialogPageId));
					break;
			}
		}

	}

	public static void onSimpleTalk(NpcAI npcAI, Creature creature) {
		if (npcAI.getOwner().getObjectTemplate().isDialogNpc()) {
			npcAI.setSubStateIfNot(AISubState.TALK);
			npcAI.getOwner().setTarget(creature);
		}
	}

	public static void onFinishTalk(NpcAI npcAI, Creature creature) {
		Npc owner = npcAI.getOwner();
		if (owner.isTargeting(creature.getObjectId())) {
			if (npcAI.getState() == AIState.FOLLOWING) {
				npcAI.think();
			} else {
				owner.setTarget(null);
			}
		}
	}

}
