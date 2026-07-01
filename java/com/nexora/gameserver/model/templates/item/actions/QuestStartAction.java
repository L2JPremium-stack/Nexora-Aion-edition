package com.nexora.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.DialogAction;
import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Nemiroff Date: 17.12.2009
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestStartAction")
public class QuestStartAction extends AbstractItemAction {

	@XmlAttribute
	protected int questid;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem, Object... params) {
		QuestState qs = player.getQuestStateList().getQuestState(questid);
		if (qs == null || qs.isStartable())
			return true;
		else if (qs.getStatus() != QuestStatus.COMPLETE)
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_QUEST_ACQUIRE_ERROR_WORKING_QUEST());
		else if (!qs.canRepeat())
			PacketSendUtility.sendPacket(player,
				SM_SYSTEM_MESSAGE.STR_QUEST_ACQUIRE_ERROR_NONE_REPEATABLE(DataManager.QUEST_DATA.getQuestById(questid).getName()));

		return false;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem, Object... params) {
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId()));
		QuestEngine.getInstance().onDialog(new QuestEnv(null, player, questid, DialogAction.ASK_QUEST_ACCEPT));
	}
}
