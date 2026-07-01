package com.nexora.gameserver.services.siege;

import java.util.Collection;
import java.util.Map;

import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.player.PlayerCommonData;
import com.nexora.gameserver.model.gameobjects.siege.SiegeNpc;
import com.nexora.gameserver.model.siege.OutpostLocation;
import com.nexora.gameserver.model.siege.SiegeModType;
import com.nexora.gameserver.model.siege.SiegeRace;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.mail.SiegeResult;
import com.nexora.gameserver.services.player.PlayerService;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.world.World;

/**
 * @author SoulKeeper, Estrayl
 */
public class OutpostSiege extends Siege<OutpostLocation> {

	public OutpostSiege(OutpostLocation siegeLocation) {
		super(siegeLocation);
	}

	@Override
	protected void onSiegeStart() {
		getSiegeLocation().setVulnerable(true);
		despawnNpcs(getSiegeLocationId());
		spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.SIEGE);
		initSiegeBoss();

		PacketSendUtility.broadcastToWorld(
			getSiegeLocationId() == 2111 ? SM_SYSTEM_MESSAGE.STR_FIELDABYSS_LIGHTBOSS_SPAWN() : SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DARKBOSS_SPAWN());
		broadcastUpdate(getSiegeLocation());
	}

	@Override
	protected void onSiegeFinish() {
		getSiegeLocation().setVulnerable(false);
		despawnSiegeNpcs();

		if (isBossKilled()) {
			onAgentDefeated();
			sendRewardsToParticipants(getWinnerRaceCounter(), SiegeResult.OCCUPY);
			sendRewardsToParticipants(getSiegeCounter().getRaceCounter(getSiegeLocationId() == 2111 ? SiegeRace.ELYOS : SiegeRace.ASMODIANS),
				SiegeResult.FAIL);
		} else {
			PacketSendUtility.broadcastToWorld(
				getSiegeLocationId() == 2111 ? SM_SYSTEM_MESSAGE.STR_FIELDABYSS_LIGHTBOSS_DESPAWN() : SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DARKBOSS_DESPAWN());
			sendRewardsToParticipants(getSiegeCounter().getRaceCounter(SiegeRace.ELYOS), SiegeResult.EMPTY);
			sendRewardsToParticipants(getSiegeCounter().getRaceCounter(SiegeRace.ASMODIANS), SiegeResult.EMPTY);
		}
		broadcastUpdate(getSiegeLocation());
		spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.PEACE);
	}

	private void onAgentDefeated() {
		SiegeRaceCounter winnerCounter = getWinnerRaceCounter();
		Map<Integer, Long> topPlayerDamages = winnerCounter.getPlayerDamageCounter();
		if (!topPlayerDamages.isEmpty()) {
			Integer topPlayerId = topPlayerDamages.keySet().iterator().next();
			PlayerCommonData pcd = PlayerService.getOrLoadPlayerCommonData(topPlayerId);
			String playerName = pcd.getName();
			String playerRace = pcd.getRace().getL10n();
			PacketSendUtility.broadcastToWorld(getSiegeLocationId() == 2111 ? SM_SYSTEM_MESSAGE.STR_FIELDABYSS_LIGHTBOSS_KILLED(playerName, playerRace)
				: SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DARKBOSS_KILLED(playerName, playerRace));
			Race winnerRace = winnerCounter.getSiegeRace() == SiegeRace.ELYOS ? Race.ELYOS : Race.ASMODIANS;

			World.getInstance().forEachPlayer(p -> {
				if (p.getRace().equals(winnerRace))
					SkillEngine.getInstance().applyEffectDirectly(winnerRace == Race.ELYOS ? 12120 : 12119, p, p);
			});
		}
	}

	public void despawnSiegeNpcs() {
		Collection<SiegeNpc> npcs = World.getInstance().getLocalSiegeNpcs(getSiegeLocationId());
		for (SiegeNpc npc : npcs) {
			if (npc != null)
				npc.getController().deleteIfAliveOrCancelRespawn();
		}
	}

	@Override
	public boolean isEndless() {
		return false;
	}

	@Override
	public void onAbyssPointsAdded(Player player, int abyssPoints) {
		if (getSiegeLocation().isVulnerable() && getSiegeLocation().isInsideLocation(player))
			getSiegeCounter().addAbyssPoints(player, abyssPoints);
	}

}
