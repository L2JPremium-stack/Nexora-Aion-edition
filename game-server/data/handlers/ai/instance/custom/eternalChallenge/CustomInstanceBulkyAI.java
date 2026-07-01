package ai.instance.custom.eternalChallenge;

import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.AttackIntention;
import com.nexora.gameserver.custom.instance.RoahCustomInstanceHandler;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.World;
import com.nexora.gameserver.world.WorldMapInstance;
import com.nexora.gameserver.world.geo.GeoService;

import ai.AggressiveNoLootNpcAI;

/**
 * @author Sykra
 */
@AIName("custom_instance_bulky")
public class CustomInstanceBulkyAI extends AggressiveNoLootNpcAI {

	public CustomInstanceBulkyAI(Npc owner) {
		super(owner);
	}

	@Override
	public AttackIntention chooseAttackIntention() {
		WorldMapInstance wmi = getPosition().getWorldMapInstance();
		if (!(wmi.getInstanceHandler() instanceof RoahCustomInstanceHandler))
			return super.chooseAttackIntention();

		VisibleObject target = getTarget();
		if (!isDead() && target != null) {
			if (!GeoService.getInstance().canSee(getOwner(), target)) {
				World.getInstance().updatePosition(getOwner(), target.getX(), target.getY(), target.getZ(), (byte) 30);
				PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
			}
		}
		return super.chooseAttackIntention();
	}

}
