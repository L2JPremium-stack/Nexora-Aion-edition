package instance.abyss;

import static com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_START_IDABRE;

import java.util.concurrent.atomic.AtomicLong;

import com.nexora.gameserver.instance.handlers.GeneralInstanceHandler;
import com.nexora.gameserver.instance.handlers.InstanceID;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.flyring.FlyRing;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.geometry.Point3D;
import com.nexora.gameserver.model.templates.flyring.FlyRingTemplate;
import com.nexora.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.world.WorldMapInstance;

/**
 * @author xTz
 */
@InstanceID(300060000)
public class SulfurTreeNestInstance extends GeneralInstanceHandler {

	private final AtomicLong startTime = new AtomicLong();
	private Race instanceRace;

	public SulfurTreeNestInstance(WorldMapInstance instance) {
		super(instance);
	}

	@Override
	public void onInstanceCreate() {
		new FlyRing(new FlyRingTemplate("SULFUR_1", mapId, new Point3D(462.9394, 380.34888, 168.97256), new Point3D(462.9394, 380.34888,
			174.97256), new Point3D(468.9229, 380.7933, 168.97256), 6), instance.getInstanceId()).spawn();
	}

	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing) {
		if (flyingRing.equals("SULFUR_1")) {
			if (startTime.compareAndSet(0, System.currentTimeMillis())) {
				PacketSendUtility.sendPacket(player, STR_MSG_INSTANCE_START_IDABRE());
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900));
				ThreadPoolManager.getInstance().schedule(() -> deleteAliveNpcs(214804, 700463, 700462, 700464, 701485, 701480), 900000);
			}
		}
		return false;
	}

	@Override
	public void onEnterInstance(Player player) {
		long start = startTime.get();
		if (start > 0) {
			long time = System.currentTimeMillis() - start;
			if (time < 900000) {
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900 - (int) time / 1000));
			}
		}

		if (instanceRace == null) {
			instanceRace = player.getRace();
			spawnGoldChest();
		}
	}

	private void spawnGoldChest() {
		spawn(instanceRace == Race.ELYOS ? 701480 : 701485, 482.87f, 474.07f, 163.16f, (byte) 90);
	}
}
