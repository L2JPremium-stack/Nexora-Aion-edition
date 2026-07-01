package ai.instance.esoterrace;

import com.nexora.gameserver.ai.AIActions;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.ai.HpPhases;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.nexora.gameserver.utils.PacketSendUtility;

import ai.AggressiveNpcAI;

/**
 * @author xTz
 */
@AIName("kexkraprototype")
public class KexkraPrototypeAI extends AggressiveNpcAI implements HpPhases.PhaseHandler {

	private final HpPhases hpPhases = new HpPhases(75);

	public KexkraPrototypeAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		hpPhases.tryEnterNextPhase(this);
	}

	@Override
	public void handleHpPhase(int phaseHpPercent) {
		getKnownList().forEachPlayer(player -> {
			if (!player.isDead()) {
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(false, 0, 0, 472, true));
			}
		});
		spawn(217206, 1320.639282f, 1171.063354f, 51.494003f, (byte) 0);
		AIActions.deleteOwner(this);
	}
}
