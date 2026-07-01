package com.nexora.gameserver.model.gameobjects;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import com.nexora.gameserver.controllers.SiegeWeaponController;
import com.nexora.gameserver.controllers.SummonController;
import com.nexora.gameserver.controllers.attack.AggroList;
import com.nexora.gameserver.controllers.attack.PlayerAggroList;
import com.nexora.gameserver.controllers.movement.SiegeWeaponMoveController;
import com.nexora.gameserver.controllers.movement.SummonMoveController;
import com.nexora.gameserver.model.CreatureType;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.SkillElement;
import com.nexora.gameserver.model.TribeClass;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.stats.container.SummonGameStats;
import com.nexora.gameserver.model.stats.container.SummonLifeStats;
import com.nexora.gameserver.model.summons.SkillOrder;
import com.nexora.gameserver.model.summons.SummonMode;
import com.nexora.gameserver.model.templates.npc.NpcTemplate;
import com.nexora.gameserver.model.templates.npc.NpcTemplateType;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.world.WorldPosition;

/**
 * @author ATracer
 */
public class Summon extends Creature {

	private final Player master;
	private SummonMode mode = SummonMode.GUARD;
	private final Queue<SkillOrder> skillOrders = new ConcurrentLinkedQueue<>();
	private Future<?> releaseTask;
	private SkillElement alwaysResistElement = SkillElement.NONE;
	private int summonedBySkillId, liveTime;

	public Summon(int objId, SummonController controller, SpawnTemplate spawnTemplate, NpcTemplate objectTemplate, Player master, int time) {
		super(objId, controller, spawnTemplate, objectTemplate, new WorldPosition(spawnTemplate.getWorldId()), true);
		controller.setOwner(this);
		moveController = controller instanceof SiegeWeaponController ? new SiegeWeaponMoveController(this) : new SummonMoveController(this);
		this.liveTime = time;
		this.master = master;
		setGameStats(new SummonGameStats(this));
		setLifeStats(new SummonLifeStats(this));
		setAlwaysResistElement(objectTemplate);
	}

	private void setAlwaysResistElement(NpcTemplate template) {
		if (template != null) {
			switch (template.getName()) {
				case "earth spirit":
					this.alwaysResistElement = SkillElement.EARTH;
					break;
				case "fire spirit":
					this.alwaysResistElement = SkillElement.FIRE;
					break;
				case "water spirit":
					this.alwaysResistElement = SkillElement.WATER;
					break;
				case "wind spirit":
					this.alwaysResistElement = SkillElement.WIND;
					break;
			}
		}
	}

	@Override
	protected AggroList createAggroList() {
		return new PlayerAggroList(this);
	}

	@Override
	public SummonGameStats getGameStats() {
		return (SummonGameStats) super.getGameStats();
	}

	@Override
	public Player getMaster() {
		return master;
	}

	@Override
	public byte getLevel() {
		return getObjectTemplate().getLevel();
	}

	@Override
	public NpcTemplate getObjectTemplate() {
		return (NpcTemplate) super.getObjectTemplate();
	}

	public int getNpcId() {
		return getObjectTemplate().getTemplateId();
	}

	public String getL10n() {
		return getObjectTemplate().getL10n();
	}

	@Override
	public NpcObjectType getNpcObjectType() {
		return NpcObjectType.SUMMON;
	}

	@Override
	public SummonController getController() {
		return (SummonController) super.getController();
	}

	public SummonMode getMode() {
		return mode;
	}

	public void setMode(SummonMode mode) {
		if (mode != SummonMode.ATTACK)
			clearSkillOrders();
		this.mode = mode;
	}

	@Override
	public boolean isEnemy(Creature creature) {
		return master.isEnemy(creature);
	}

	@Override
	public boolean isEnemyFrom(Npc npc) {
		return master.isEnemyFrom(npc);
	}

	@Override
	public boolean isEnemyFrom(Player player) {
		return master.isEnemyFrom(player);
	}

	@Override
	public boolean isPvpTarget(Creature creature) {
		return creature.getActingCreature() instanceof Player;
	}

	@Override
	public TribeClass getTribe() {
		return master.getTribe();
	}

	public CreatureType getType(Creature creature) {
		boolean friend = master.getRace() == creature.getRace() && !creature.isEnemy(master);
		return friend ? CreatureType.SUPPORT : CreatureType.ATTACKABLE;
	}

	@Override
	public SummonMoveController getMoveController() {
		return (SummonMoveController) super.getMoveController();
	}

	@Override
	public Player getActingCreature() {
		return getMaster();
	}

	@Override
	public Race getRace() {
		return getMaster().getRace();
	}

	public boolean isPet() {
		return getObjectTemplate().getNpcTemplateType() == NpcTemplateType.SUMMON_PET;
	}

	/**
	 * @return liveTime in sec.
	 */
	public int getLiveTime() {
		return liveTime;
	}

	/**
	 * @param liveTime
	 *          in sec.
	 */
	public void setLiveTime(int liveTime) {
		this.liveTime = liveTime;
	}

	public int getSummonedBySkillId() {
		return summonedBySkillId;
	}

	public void setSummonedBySkillId(int summonedBySkillId) {
		this.summonedBySkillId = summonedBySkillId;
	}

	public void setReleaseTask(Future<?> task) {
		releaseTask = task;
	}

	public void cancelReleaseTask() {
		if (releaseTask != null && !releaseTask.isDone()) {
			releaseTask.cancel(true);
		}
	}

	public void addSkillOrder(int skillId, int skillLvl, Creature target, int hate, boolean release) {
		skillOrders.add(new SkillOrder(skillId, skillLvl, target, hate, release));
	}

	public SkillOrder retrieveNextSkillOrder() {
		return skillOrders.poll();
	}

	public SkillOrder getNextSkillOrder() {
		return skillOrders.peek();
	}

	public void clearSkillOrders() {
		skillOrders.clear();
	}

	public SkillElement getAlwaysResistElement() {
		return alwaysResistElement;
	}
}
