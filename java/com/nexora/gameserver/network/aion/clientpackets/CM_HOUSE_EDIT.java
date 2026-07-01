package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.gameobjects.HouseDecoration;
import com.nexora.gameserver.model.gameobjects.HouseObject;
import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.Persistable.PersistentState;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.house.House;
import com.nexora.gameserver.model.templates.item.ItemTemplate;
import com.nexora.gameserver.model.templates.item.actions.DecorateAction;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_HOUSE_EDIT;
import com.nexora.gameserver.network.aion.serverpackets.SM_HOUSE_REGISTRY;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.services.HousingService;
import com.nexora.gameserver.services.item.HouseObjectFactory;
import com.nexora.gameserver.services.item.ItemPacketService.ItemDeleteType;
import com.nexora.gameserver.utils.audit.AuditLogger;
import com.nexora.gameserver.utils.idfactory.IDFactory;

/**
 * @author Rolandas
 */
public class CM_HOUSE_EDIT extends AionClientPacket {

	private int action;
	private int itemObjectId;
	private float x, y, z;
	private int rotation;
	private int buildingId;

	public CM_HOUSE_EDIT(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		action = readUC();
		if (action == 3) {
			itemObjectId = readD();
		} else if (action == 4) {
			itemObjectId = readD();
		} else if (action == 5) {
			itemObjectId = readD();
			x = readF();
			y = readF();
			z = readF();
			rotation = readUH();
		} else if (action == 6) {
			itemObjectId = readD();
			x = readF();
			y = readF();
			z = readF();
			rotation = readUH();
		} else if (action == 7) {
			itemObjectId = readD();
		} else if (action == 16) {
			buildingId = readD();
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null)
			return;
		House house = player.getActiveHouse();

		if (action == 1) { // Enter Decoration mode
			sendPacket(new SM_HOUSE_EDIT(action));
			sendPacket(new SM_HOUSE_REGISTRY(action));
			sendPacket(new SM_HOUSE_REGISTRY(action + 1));
		} else if (action == 2) { // Exit Decoration mode
			sendPacket(new SM_HOUSE_EDIT(action));
		} else if (action == 3) { // Add item
			Item item = player.getInventory().getItemByObjId(itemObjectId);
			if (item == null)
				return;

			ItemTemplate template = item.getItemTemplate();
			player.getInventory().delete(item, ItemDeleteType.REGISTER);

			DecorateAction decorateAction = template.getActions().getDecorateAction();
			if (decorateAction != null) {
				HouseDecoration decor = new HouseDecoration(IDFactory.getInstance().nextId(), decorateAction.getTemplateId());
				house.getRegistry().putDecor(decor, true);
				sendPacket(new SM_HOUSE_EDIT(action, 2, decor.getObjectId()));
			} else {
				HouseObject<?> obj = HouseObjectFactory.createNew(house, template);
				house.getRegistry().putObject(obj, true);
				sendPacket(new SM_HOUSE_EDIT(action, 1, obj.getObjectId()));
			}
		} else if (action == 4) { // Delete item
			house.getRegistry().discardObject(house.getRegistry().getObjectByObjId(itemObjectId), false);
			sendPacket(new SM_HOUSE_EDIT(action, 1, itemObjectId));
			sendPacket(new SM_HOUSE_EDIT(4, 1, itemObjectId));
		} else if (action == 5) { // spawn object
			HouseObject<?> obj = house.getRegistry().getObjectByObjId(itemObjectId);
			if (obj == null)
				return;
			obj.setX(x);
			obj.setY(y);
			obj.setZ(z);
			obj.setRotation(rotation);
			sendPacket(new SM_HOUSE_EDIT(action, itemObjectId, x, y, z, rotation));
			obj.spawn();
			house.getRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
			sendPacket(new SM_HOUSE_EDIT(4, 1, itemObjectId));
			QuestEngine.getInstance().onHouseItemUseEvent(new QuestEnv(null, player, 0));
		} else if (action == 6) { // move object
			HouseObject<?> obj = house.getRegistry().getObjectByObjId(itemObjectId);
			if (obj == null)
				return;
			sendPacket(new SM_HOUSE_EDIT(action + 1, 0, itemObjectId));
			obj.getController().delete();
			obj.setX(x);
			obj.setY(y);
			obj.setZ(z);
			obj.setRotation(rotation);
			if (obj.getPersistentState() == PersistentState.UPDATE_REQUIRED)
				house.getRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
			sendPacket(new SM_HOUSE_EDIT(action - 1, itemObjectId, x, y, z, rotation));
			obj.spawn();
		} else if (action == 7) { // despawn object
			HouseObject<?> obj = house.getRegistry().getObjectByObjId(itemObjectId);
			if (obj == null)
				return;
			sendPacket(new SM_HOUSE_EDIT(action, 0, itemObjectId));
			obj.removeFromHouse();
			sendPacket(new SM_HOUSE_EDIT(3, 1, itemObjectId)); // place it back
		} else if (action == 14) { // enter renovation mode
			sendPacket(new SM_HOUSE_EDIT(14));
		} else if (action == 15) { // exit renovation mode
			sendPacket(new SM_HOUSE_EDIT(15));
		} else if (action == 16) {
			if (!removeRenovationCoupon(player, house)) {
				AuditLogger.log(player, "attempted house renovation without coupon");
				return;
			}
			HousingService.getInstance().switchHouseBuilding(house, buildingId);
			house.getController().updateAppearance();
		}
	}

	private boolean removeRenovationCoupon(Player player, House house) {
		int typeId = house.getHouseType().getId();
		if (typeId == 0)
			return false; // studio
		int itemId = (player.getRace().equals(Race.ELYOS) ? 169661004 : 169661008) - typeId;
		if (player.getInventory().getItemCountByItemId(itemId) > 0)
			return player.getInventory().decreaseByItemId(itemId, 1);
		return false;
	}
}
