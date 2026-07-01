package instance.pvparenas;

import com.nexora.gameserver.instance.handlers.InstanceID;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.instance.InstanceScoreType;
import com.nexora.gameserver.network.aion.instanceinfo.ArenaScoreWriter;
import com.nexora.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.WorldMapInstance;

/**
 * @author xTz
 */
@InstanceID(300430000)
public class DisciplineTrainingGroundsInstance extends PvPArenaInstance {

	public DisciplineTrainingGroundsInstance(WorldMapInstance instance) {
		super(instance);
	}

	protected void setScoreCaps() {
		instanceScore.setLowerScoreCap(10000);
		instanceScore.setUpperScoreCap(50000);
		instanceScore.setMaxScoreGap(1500);
	}

	@Override
	public void onInstanceCreate() {
		pointsPerKill = 200;
		pointsPerDeath = -100;
		super.onInstanceCreate();
	}

	@Override
	protected int getBoostMoraleEffectDuration(int rank) {
		return switch (rank) {
			case 0 -> 14000;
			case 1 -> 16000;
			default -> 15000;
		};
	}

	@Override
	protected void sendPacket(Player player, InstanceScoreType scoreType) {
		instance.forEachPlayer(
			p -> PacketSendUtility.sendPacket(p, new SM_INSTANCE_SCORE(instance.getMapId(), new ArenaScoreWriter(instanceScore, p.getObjectId(), true))));
	}
}
