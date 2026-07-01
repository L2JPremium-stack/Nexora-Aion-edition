package com.nexora.gameserver.network.aion.serverpackets;

import com.nexora.gameserver.model.gameobjects.Summon;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.AionServerPacket;
import com.nexora.gameserver.utils.stats.CalculationType;

/**
 * @author ATracer, xTz
 */
public class SM_SUMMON_PANEL extends AionServerPacket {

	private Summon summon;

	public SM_SUMMON_PANEL(Summon summon) {
		this.summon = summon;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(summon.getObjectId());
		writeH(summon.getLevel());
		writeD(0);// unk
		writeD(0);// unk
		writeD(summon.getLifeStats().getCurrentHp());
		writeD(summon.getGameStats().getMaxHp().getCurrent());
		writeD(summon.getGameStats().getMainHandPAttack(CalculationType.DISPLAY).getCurrent());
		writeH(summon.getGameStats().getPDef().getCurrent());
		writeH(0);
		writeH(summon.getGameStats().getMResist().getCurrent());
		writeH(0);// unk
		writeH(0);// unk
		writeD(summon.getLiveTime()); // life time
	}

}
