package admincommands;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.HouseObject;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.housing.PlaceableHouseObject;
import com.nexora.gameserver.model.templates.item.ItemTemplate;
import com.nexora.gameserver.model.templates.item.actions.ItemActions;
import com.nexora.gameserver.model.templates.item.actions.SummonHouseObjectAction;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.spawnengine.SpawnEngine;
import com.nexora.gameserver.utils.ChatUtil;
import com.nexora.gameserver.utils.chathandlers.AdminCommand;
import com.nexora.gameserver.utils.idfactory.IDFactory;
import com.nexora.gameserver.world.World;

/**
 * @author Luno, Neon
 */
public class SpawnNpc extends AdminCommand {

	public SpawnNpc() {
		super("spawn", "Spawns npcs and gatherables.");

		// @formatter:off
		setSyntaxInfo(
			"<id> - Spawns a temporary object with the specified template ID.",
			"<id> <static id> [respawn time] - Spawns an object with the specified ID and static ID (default: temporary spawn, optional: respawn time in seconds).",
			"<item link|ID> - Spawns the house object from given item link or ID."
		);
		// @formatter:on
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length < 1) {
			sendInfo(admin);
			return;
		}

		int itemId = ChatUtil.getItemId(params[0]);
		if (itemId > 0) {
			spawnHouseObject(admin, itemId);
			return;
		}

		int npcId = Integer.parseInt(params[0]);
		int staticId = params.length < 2 ? 0 : Integer.parseInt(params[1]);
		int respawnTime = params.length < 3 ? 0 : Integer.parseInt(params[2]);

		if (DataManager.NPC_DATA.getNpcTemplate(npcId) == null && DataManager.GATHERABLE_DATA.getGatherableTemplate(npcId) == null) {
			sendInfo(admin, "Invalid npc ID.");
			return;
		}
		if (staticId < 0) {
			sendInfo(admin, "Invalid static ID.");
			return;
		}
		if (respawnTime < 0) {
			sendInfo(admin, "Invalid respawn time.");
			return;
		}

		SpawnTemplate st = SpawnEngine.newSpawn(admin.getWorldId(), npcId, admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(), respawnTime);
		st.setStaticId(staticId);
		VisibleObject visibleObject = SpawnEngine.spawnObject(st, admin.getInstanceId());
		if (respawnTime > 0 && !DataManager.SPAWNS_DATA.saveSpawn(visibleObject, false))
			sendInfo(admin, "Could not save spawn. Npc will vanish after server restart.");
	}

	private void spawnHouseObject(Player admin, int itemId) {
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		ItemActions actions = itemTemplate == null ? null : itemTemplate.getActions();
		SummonHouseObjectAction action = actions == null ? null : actions.getHouseObjectAction();
		if (action == null) {
			sendInfo(admin, "Item is not a spawnable house item.");
			return;
		}

		HouseObject<PlaceableHouseObject> houseObject = new DummyHouseObject(action.getTemplateId());
		houseObject.setPosition(
				World.getInstance().createPosition(admin.getWorldId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(), admin.getInstanceId()));
		SpawnEngine.bringIntoWorld(houseObject);
	}

	private static class DummyHouseObject extends HouseObject<PlaceableHouseObject> {

		public DummyHouseObject(int templateId) {
			super(null, IDFactory.getInstance().nextId(), templateId, true);
		}

		@Override
		public float getX() {
			return getPosition().getX();
		}

		@Override
		public float getY() {
			return getPosition().getY();
		}

		@Override
		public float getZ() {
			return getPosition().getZ();
		}

		@Override
		public byte getHeading() {
			return getPosition().getHeading();
		}
	}
}
