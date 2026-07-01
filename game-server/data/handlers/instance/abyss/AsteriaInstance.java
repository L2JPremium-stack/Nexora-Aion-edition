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
 * @author bobobear
 */
@InstanceID(300050000)
public class AsteriaInstance extends GeneralInstanceHandler {

	private AtomicLong startTime = new AtomicLong();
	private Race instanceRace;

	public AsteriaInstance(WorldMapInstance instance) {
		super(instance);
	}

	@Override
	public void onInstanceCreate() {
		new FlyRing(new FlyRingTemplate("ASTERIA_WING_1", mapId, new Point3D(479.24, 572.57, 202.72), new Point3D(477.95, 567.64, 212.9),
			new Point3D(477.97, 563.35, 202.12), 10), instance.getInstanceId()).spawn();
	}

	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing) {
		if (flyingRing.equals("ASTERIA_WING_1")) {
			if (startTime.compareAndSet(0, System.currentTimeMillis())) {
				PacketSendUtility.sendPacket(player, STR_MSG_INSTANCE_START_IDABRE());
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900));
				ThreadPoolManager.getInstance().schedule(() -> deleteAliveNpcs(700475, 700476, 700477, 701483, 701488), 900000);
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
		spawn(instanceRace == Race.ELYOS ? 701483 : 701488, 512.8f, 565.35f, 198f, (byte) 60);
	}
}
