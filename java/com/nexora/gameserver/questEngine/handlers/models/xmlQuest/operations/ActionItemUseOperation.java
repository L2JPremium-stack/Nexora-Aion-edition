package com.nexora.gameserver.questEngine.handlers.models.xmlQuest.operations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.EmotionType;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.nexora.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionItemUseOperation", propOrder = { "finish" })
public class ActionItemUseOperation extends QuestOperation {

	@XmlElement(required = true)
	protected QuestOperations finish;

	@Override
	public void doOperate(final QuestEnv env) {
		final Player player = env.getPlayer();
		final Npc npc;
		if (env.getVisibleObject() instanceof Npc)
			npc = (Npc) env.getVisibleObject();
		else
			return;
		final int defaultUseTime = 3000;
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), npc.getObjectId(), defaultUseTime, 1));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, npc.getObjectId()), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), npc.getObjectId(), defaultUseTime, 0));
				finish.operate(env);
			}
		}, defaultUseTime);

	}

}
