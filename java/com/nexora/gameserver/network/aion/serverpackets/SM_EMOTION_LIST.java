package com.nexora.gameserver.network.aion.serverpackets;

import java.util.Collection;

import com.nexora.gameserver.model.gameobjects.player.emotion.Emotion;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;

public class SM_EMOTION_LIST extends AionServerPacket {

	private final byte action;
	private final Collection<Emotion> emotions;

	public SM_EMOTION_LIST(byte action, Collection<Emotion> emotions) {
		this.action = action;
		this.emotions = emotions;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(action);
		writeH(emotions.size());
		for (Emotion emotion : emotions) {
			writeD(emotion.getId());
			writeH(emotion.secondsUntilExpiration());
		}
	}
}
