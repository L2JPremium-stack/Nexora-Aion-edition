package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.QuestTemplate;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.nexora.gameserver.services.QuestService;

public class CM_DELETE_QUEST extends AionClientPacket {

	private int questId;

	public CM_DELETE_QUEST(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		questId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		QuestTemplate qt = DataManager.QUEST_DATA.getQuestById(questId);

		if (qt != null && qt.isTimer()) {
			player.getController().cancelTask(TaskId.QUEST_TIMER);
			sendPacket(new SM_QUEST_ACTION(questId, 0));
		}
		QuestService.abandonQuest(player, questId);
	}
}
