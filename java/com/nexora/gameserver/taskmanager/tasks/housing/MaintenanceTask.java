package com.nexora.gameserver.taskmanager.tasks.housing;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.nexora.gameserver.configs.main.HousingConfig;
import com.nexora.gameserver.model.gameobjects.player.PlayerCommonData;
import com.nexora.gameserver.model.house.House;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.HousingBidService;
import com.nexora.gameserver.services.HousingService;
import com.nexora.gameserver.services.mail.MailFormatter;
import com.nexora.gameserver.services.player.PlayerService;
import com.nexora.gameserver.taskmanager.AbstractCronTask;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * Handles house maintenance as well as impoundment if a player didn't pay for two weeks.
 * 
 * @author Rolandas, Neon
 */
public class MaintenanceTask extends AbstractCronTask {

	private static final MaintenanceTask instance = new MaintenanceTask();

	public static MaintenanceTask getInstance() {
		return instance;
	}

	private MaintenanceTask() {
		super(HousingConfig.HOUSE_MAINTENANCE_TIME);
	}

	@Override
	protected void executeTask() {
		List<House> housesToMaintain = findHousesToMaintain();
		log.info("Executing house maintenance for " + housesToMaintain.size() + " houses");

		Date now = new Date();
		for (House house : housesToMaintain) {
			if (house.getNextPay() == null) { // the first week is free for newly acquired houses
				house.setNextPay(getNextRun());
				house.save();
				continue;
			} else if (house.getNextPay().after(now))
				continue;

			PlayerCommonData pcd = PlayerService.getOrLoadPlayerCommonData(house.getOwnerId());
			if (pcd == null) { // player got deleted
				putHouseToAuction(house, null);
				continue;
			}

			String ownerName = house.getOwnerName();
			long compensationKinah = 0;
			Date impoundDate = calculateImpoundDate(house.getNextPay());
			if (impoundDate.getTime() <= System.currentTimeMillis()) {
				putHouseToAuction(house, pcd);
				// return 90% of the house cost (https://aion.fandom.com/wiki/Housing)
				compensationKinah = (long) (house.getDefaultAuctionPrice() * 0.9f);
			}

			MailFormatter.sendHouseMaintenanceMail(house, ownerName, impoundDate.getTime(), compensationKinah);
		}
	}

	private Date calculateImpoundDate(Timestamp housePaidUntil) {
		Date paymentDueDate = Date.from(housePaidUntil.toInstant().plus(14, ChronoUnit.DAYS)); // player must pay within two weeks
		Date impoundDate = new Date();
		while (impoundDate.before(paymentDueDate)) {
			impoundDate = getNextRunAfter(impoundDate);
		}
		return impoundDate;
	}

	private List<House> findHousesToMaintain() {
		if (!HousingConfig.ENABLE_HOUSE_PAY)
			return Collections.emptyList();
		return HousingService.getInstance().getCustomHouses().stream()
			.filter(house -> !house.isInactive() && house.getOwnerId() != 0).collect(Collectors.toList());
	}

	private void putHouseToAuction(House house, PlayerCommonData owner) {
		HousingService.getInstance().changeOwner(house, 0);
		HousingBidService.getInstance().auction(house, house.getDefaultAuctionPrice());
		log.info("Auctioned house " + house.getAddress().getId() + " because " + (owner == null ? "owner got deleted." : "maintenance fee was overdue."));
		if (owner != null && owner.isOnline())
			PacketSendUtility.sendPacket(owner.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_SEQUESTRATE());
	}

}
