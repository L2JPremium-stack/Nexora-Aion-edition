package com.nexora.gameserver.questEngine.handlers.models.xmlQuest.operations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.nexora.gameserver.network.aion.serverpackets.SM_QUEST_ACTION.ActionType;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetQuestStatusOperation")
public class SetQuestStatusOperation extends QuestOperation {

	@XmlAttribute(required = true)
	protected QuestStatus status;

	@Override
	public void doOperate(QuestEnv env) {
		Player player = env.getPlayer();
		int questId = env.getQuestId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null) {
			qs.setStatus(status);
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(ActionType.UPDATE, qs));
			if (qs.getStatus() == QuestStatus.COMPLETE)
				player.getController().updateNearbyQuests();
		}
	}
}
