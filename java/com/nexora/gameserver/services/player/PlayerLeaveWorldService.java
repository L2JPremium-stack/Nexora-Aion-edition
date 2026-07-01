package com.nexora.gameserver.services.player;

import java.sql.Timestamp;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexora.gameserver.configs.main.AutoGroupConfig;
import com.nexora.gameserver.dao.*;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.dataholders.PlayerInitialData.LocationData;
import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.Summon;
import com.nexora.gameserver.model.gameobjects.player.BindPointPosition;
import com.nexora.gameserver.model.gameobjects.player.FriendList;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.summons.SummonMode;
import com.nexora.gameserver.model.summons.UnsummonType;
import com.nexora.gameserver.model.team.alliance.PlayerAllianceService;
import com.nexora.gameserver.model.team.group.PlayerGroupService;
import com.nexora.gameserver.network.aion.AionConnection;
import com.nexora.gameserver.network.aion.clientpackets.CM_QUIT;
import com.nexora.gameserver.network.chatserver.ChatServer;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.services.*;
import com.nexora.gameserver.services.conquerorAndProtectorSystem.ConquerorAndProtectorService;
import com.nexora.gameserver.services.findgroup.FindGroupService;
import com.nexora.gameserver.services.instance.InstanceService;
import com.nexora.gameserver.services.summons.SummonsService;
import com.nexora.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.utils.audit.GMService;
import com.nexora.gameserver.world.World;
import com.nexora.gameserver.world.WorldPosition;

/**
 * @author ATracer, Neon
 */
public class PlayerLeaveWorldService {

	private static final Logger log = LoggerFactory.getLogger(PlayerLeaveWorldService.class);

	/**
	 * This method is called when a player loses client connection, e.g. when killing the process, or due to bad network connectivity.<br>
	 * <br>
	 * <b><font color='red'>NOTICE:</font> This method must only be called from {@link AionConnection#onDisconnect()} and not from anywhere else</b>
	 * 
	 * @see #leaveWorld(Player)
	 */
	public static void leaveWorldDelayed(Player player, long delayInMillis) {
		Future<?> leaveWorldTask = ThreadPoolManager.getInstance().schedule(() -> leaveWorld(player), delayInMillis);
		player.getController().addTask(TaskId.DESPAWN, leaveWorldTask);
	}

	/**
	 * This method saves a player and removes him from the world. It is called when a player leaves the game, which includes just two cases: either
	 * he goes back to char selection screen or is leaving the game (closing client).<br>
	 * <br>
	 * <b><font color='red'>NOTICE:</font> This method is called only from {@link CM_QUIT} and must not be called from anywhere else</b>
	 */
	public static void leaveWorld(Player player) {
		AionConnection con = player.getClientConnection();
		player.setClientConnection(null); // this sets the player semi-offline, PacketSendUtility will not send packets anymore

		WorldPosition pos = player.getPosition();
		if (pos == null || pos.getMapRegion() == null) { // ensure safe logout
			log.warn(player + " had invalid position: " + pos + " so he was reset to bind point");
			BindPointPosition bp = player.getBindPoint();
			if (bp != null)
				pos = World.getInstance().createPosition(bp.getMapId(), bp.getX(), bp.getY(), bp.getZ(), bp.getHeading(), 1);
			else {
				LocationData ld = DataManager.PLAYER_INITIAL_DATA.getSpawnLocation(player.getRace());
				pos = World.getInstance().createPosition(ld.getMapId(), ld.getX(), ld.getY(), ld.getZ(), ld.getHeading(), 1);
			}
			player.setPosition(pos);
		}

		FindGroupService.getInstance().onLogout(player);
		player.getResponseRequester().denyAll();
		player.getFriendList().setStatus(FriendList.Status.OFFLINE, player.getCommonData());
		BrokerService.getInstance().removePlayerCache(player);
		ExchangeService.getInstance().cancelExchange(player);
		RepurchaseService.getInstance().removeRepurchaseItems(player);
		if (AutoGroupConfig.AUTO_GROUP_ENABLE)
			AutoGroupService.getInstance().onLogout(player);
		ConquerorAndProtectorService.getInstance().onLeaveMap(player);
		MultiClientingService.onLeaveWorld(player);
		InstanceService.onLogout(player);
		GMService.getInstance().onPlayerLogout(player);
		KiskService.getInstance().onLogout(player);

		if (player.isDead()) {
			if (player.isInInstance() || player.getWorldId() == 400030000)
				PlayerReviveService.instanceRevive(player);
			else
				PlayerReviveService.bindRevive(player);
		} else if (DuelService.getInstance().isDueling(player)) {
			DuelService.getInstance().loseDuel(player);
		}
		player.getEffectController().removeNonStorableEffectsForLogout();
		PlayerEffectsDAO.storePlayerEffects(player);
		PlayerCooldownsDAO.storePlayerCooldowns(player);
		ItemCooldownsDAO.storeItemCooldowns(player);
		PlayerLifeStatsDAO.updatePlayerLifeStat(player);

		PlayerGroupService.onPlayerLogout(player);
		PlayerAllianceService.onPlayerLogout(player);
		// fix legion warehouse exploits
		LegionService.getInstance().LegionWhUpdate(player);
		player.getEffectController().removeAllEffects(true);
		player.getLifeStats().cancelAllTasks();

		Summon summon = player.getSummon();
		if (summon != null)
			SummonsService.doMode(SummonMode.RELEASE, summon, UnsummonType.LOGOUT);
		if (player.getPet() != null)
			player.getPet().getController().delete();
		if (player.getPostman() != null)
			player.getPostman().getController().delete();

		ExpireTimerTask.getInstance().unregisterExpirables(player);
		if (player.getCraftingTask() != null)
			player.getCraftingTask().stop();

		QuestEngine.getInstance().onLogOut(new QuestEnv(null, player, 0));
		Timestamp lastOnline = new Timestamp(System.currentTimeMillis());
		player.getController().delete();
		player.getCommonData().setOnline(false);
		player.getCommonData().setLastOnline(lastOnline);
		if (player.isLegionMember()) // must be called after setOnline and setLastOnline
			LegionService.getInstance().onLogout(player);
		player.getCommonData().setX(player.getX());
		player.getCommonData().setY(player.getY());
		player.getCommonData().setZ(player.getZ());
		player.getCommonData().setHeading(player.getHeading());

		ChatServer.getInstance().sendPlayerLogout(player);

		PlayerService.storePlayer(player);

		player.getInventory().setOwner(null);
		player.getWarehouse().setOwner(null);
		player.getAccount().getAccountWarehouse().setOwner(null);

		PlayerDAO.storeOldCharacterLevel(player.getObjectId(), player.getLevel());
		PlayerDAO.storeLastOnlineTime(player.getObjectId(), lastOnline);
		PlayerDAO.onlinePlayer(player, false); // marks that player was fully saved and may enter world again

		con.setActivePlayer(null);
	}
}
