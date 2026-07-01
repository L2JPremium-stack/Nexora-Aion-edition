package com.nexora.gameserver.model.gameobjects;

import com.nexora.gameserver.controllers.NpcController;
import com.nexora.gameserver.controllers.effect.EffectController;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.CreatureType;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.house.House;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.world.knownlist.PlayerAwareKnownList;

/**
 * @author Rolandas
 */
public class SummonedHouseNpc extends SummonedObject<House> {

	public SummonedHouseNpc(NpcController controller, SpawnTemplate spawnTemplate, House house) {
		super(controller, spawnTemplate, DataManager.NPC_DATA.getNpcTemplate(spawnTemplate.getNpcId()).getLevel(), house);
		String masterName = house.getOwnerName();
		setMasterName(masterName == null ? "" : masterName);
		setKnownlist(new PlayerAwareKnownList(this));
		setEffectController(new EffectController(this));
	}

	@Override
	public int getCreatorId() {
		return getCreator().getAddress().getId();
	}

	@Override
	public boolean isEnemy(Creature creature) {
		return false;
	}

	@Override
	public boolean isEnemyFrom(Npc npc) {
		return false;
	}

	@Override
	public boolean isEnemyFrom(Player player) {
		return false;
	}

	@Override
	public CreatureType getType(Creature creature) {
		return CreatureType.FRIEND;
	}

}
