package com.nexora.gameserver.network.aion.instanceinfo;

import java.nio.ByteBuffer;

import com.nexora.gameserver.model.instance.instancescore.InstanceScore;
import com.nexora.gameserver.network.PacketWriteHelper;

/**
 * @author xTz
 */
public abstract class InstanceScoreWriter<T extends InstanceScore<?>> extends PacketWriteHelper {

	protected final T instanceScore;

	public InstanceScoreWriter(T instanceScore) {
		this.instanceScore = instanceScore;
	}

	public T getInstanceScore() {
		return instanceScore;
	}

	@Override
	public void writeMe(ByteBuffer buf) {
	}

}
