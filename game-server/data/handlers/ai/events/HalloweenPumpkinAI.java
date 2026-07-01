package ai.events;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.ai.AIName;
import com.nexora.gameserver.controllers.NpcController;
import com.nexora.gameserver.controllers.effect.EffectController;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.drop.DropItem;
import com.nexora.gameserver.model.gameobjects.DropNpc;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.stats.calc.Stat2;
import com.nexora.gameserver.model.stats.container.StatEnum;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.services.drop.DropRegistrationService;
import com.nexora.gameserver.spawnengine.SpawnEngine;
import com.nexora.gameserver.world.knownlist.NpcKnownList;

import ai.OneDmgAI;

@AIName("halloween_pumpkin")
public class HalloweenPumpkinAI extends OneDmgAI {

	private static final Map<Integer, Long> nextRewardMillisByAccountId = new ConcurrentHashMap<>();

	public HalloweenPumpkinAI(Npc owner) {
		super(owner);
	}

	@Override
	protected void handleSpawned() {
		if (getNpcId() != 219484 && Rnd.chance() < 33) {
			replaceWithLargePumpkin();
			return;
		}
		super.handleSpawned();
	}

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	public void modifyOwnerStat(Stat2 stat) {
		if (stat.getStat() == StatEnum.MAXHP)
			stat.setBase(10);
	}

	@Override
	protected void handleDropRegistered() {
		if (getNpcId() == 219484) {
			float chance = Rnd.chance();
			if (chance < 1 && getAggroList().getMostPlayerDamage() instanceof Player p && canReward(p))
				replaceDropWithItem(162000131); // Fine Bracing Water
			else if (chance < 3)
				tryReplaceDropWithWeaponPrototype();
		}
	}

	private boolean canReward(Player player) {
		AtomicBoolean canReward = new AtomicBoolean();
		nextRewardMillisByAccountId.compute(player.getAccount().getId(), (_, nextRewardTime) -> {
			long nowMs = System.currentTimeMillis();
			if (nextRewardTime == null || nowMs > nextRewardTime) {
				nextRewardTime = nowMs + TimeUnit.DAYS.toMillis(1);
				canReward.set(true);
			}
			return nextRewardTime;
		});
		return canReward.get();
	}

	private void replaceDropWithItem(int itemId) {
		Set<DropItem> drops = DropRegistrationService.getInstance().getCurrentDropMap().get(getObjectId());
		if (drops == null)
			return;
		drops.clear();
		drops.add(DropRegistrationService.getInstance().regDropItem(1, 0, getObjectId(), itemId, 1));
	}

	private void tryReplaceDropWithWeaponPrototype() {
		DropNpc dropNpc = DropRegistrationService.getInstance().getDropRegistrationMap().get(getObjectId());
		if (dropNpc == null)
			return;
		Integer prototypeItemId = selectWeaponPrototypeItemId(dropNpc.getAllowedLooters());
		if (prototypeItemId != null)
			replaceDropWithItem(prototypeItemId);
	}

	private Integer selectWeaponPrototypeItemId(Set<Integer> allowedLooters) {
		List<Integer> itemIds = allowedLooters.stream()
			.map(id -> getKnownList().getObject(id) instanceof Player player ? player : null)
			.filter(Objects::nonNull)
			.flatMap(this::getWeaponPrototypeItemIds)
			.toList();
		return Rnd.get(itemIds);
	}

	private Stream<Integer> getWeaponPrototypeItemIds(Player player) {
		return switch (player.getPlayerClass()) {
			case GLADIATOR -> Stream.of(169405391, 169405395); // Upgraded Distorted Sword/Polearm of the Fierce Battle Prototype
			case TEMPLAR -> Stream.of(169405391, 169405394); // Upgraded Distorted Sword/Greatsword of the Fierce Battle Prototype
			case ASSASSIN, RANGER -> Stream.of(169405391, 169405393); // Upgraded Distorted Sword/Dagger of the Fierce Battle Prototype
			case CLERIC, CHANTER -> Stream.of(169405392, 169405396); // Upgraded Distorted Mace/Staff of the Fierce Battle Prototype
			default -> Stream.empty();
		};
	}

	private void replaceWithLargePumpkin() {
		getOwner().getController().delete();
		SpawnTemplate spot = getSpawnTemplate();
		if (getSpawnTemplate().hasPool()) // despawning frees its pool spot, so we cannot reuse it
			spot = getSpawnTemplate().getGroup().reserveRandomFreePoolSpot(getOwner().getInstanceId());
		Npc largePumpkin = new Npc(new NpcController(), spot, DataManager.NPC_DATA.getNpcTemplate(219484));
		largePumpkin.setKnownlist(new NpcKnownList(largePumpkin));
		largePumpkin.setEffectController(new EffectController(largePumpkin));
		SpawnEngine.bringIntoWorld(largePumpkin, spot.getWorldId(), getOwner().getInstanceId(), spot.getX(), spot.getY(), spot.getZ(), spot.getHeading());
	}
}
