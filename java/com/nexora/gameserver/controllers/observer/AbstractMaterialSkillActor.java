package com.nexora.gameserver.controllers.observer;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import com.nexora.gameserver.configs.main.GeoDataConfig;
import com.nexora.gameserver.geoEngine.scene.Spatial;
import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.materials.MaterialActCondition;
import com.nexora.gameserver.model.templates.materials.MaterialSkill;
import com.nexora.gameserver.model.templates.world.WeatherEntry;
import com.nexora.gameserver.services.GameTimeService;
import com.nexora.gameserver.services.WeatherService;
import com.nexora.gameserver.skillengine.SkillEngine;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.utils.time.gametime.DayTime;

/**
 * @author Yeats, Neon
 */
public abstract class AbstractMaterialSkillActor extends AbstractCollisionObserver {

	private final AtomicReference<Future<?>> task = new AtomicReference<>();
	private final TaskId taskId;
	protected volatile List<MaterialSkill> skills;
	protected volatile boolean isTouched = false;

	public AbstractMaterialSkillActor(Creature creature, Spatial geometry, byte intentions, CheckType checkType, TaskId taskId, List<MaterialSkill> skills) {
		super(creature, geometry, intentions, checkType);
		this.taskId = taskId;
		this.skills = skills;
	}

	public void act() {
		if (!skills.isEmpty() && !creature.getController().hasTask(taskId)) {
			Future<?> t = ThreadPoolManager.getInstance().scheduleAtFixedRate(new MaterialSkillTask(), 0, 1000);
			if (task.compareAndSet(null, t))
				creature.getController().addTask(taskId, task.get());
			else // should not happen
				t.cancel(false);
		}
	}

	public void abort() {
		Future<?> t = task.getAndSet(null);
		if (t != null)
			creature.getController().cancelTaskIfPresent(taskId, t);
	}

	@Override
	public void died(Creature creature) {
		isTouched = false;
		abort();
	}

	private MaterialSkill findFirstSkillWithMatchingCondition() {
		synchronized (skills) {
			for (MaterialSkill skill : skills) {
				if (matchActConditions(skill))
					return skill;
			}
		}
		return null;
	}

	private boolean matchActConditions(MaterialSkill skill) {
		if (skill.getConditions().isEmpty())
			return true;
		for (MaterialActCondition condition : skill.getConditions()) {
			if (condition == MaterialActCondition.NIGHT && GameTimeService.getInstance().getGameTime().getDayTime() == DayTime.NIGHT)
				return true;
			if (condition == MaterialActCondition.SUNNY) { // sunny actually means "not raining" (fireplaces don't burn during rain)
				WeatherEntry weatherEntry = WeatherService.getInstance().findWeatherEntry(creature);
				boolean isRain = weatherEntry.getWeatherName() != null && weatherEntry.getWeatherName().startsWith("RAIN");
				if (!isRain || weatherEntry.isBefore()) // before means "before" the weather (e.g. clouds before rain)
					return true;
			}
		}
		return false;
	}

	private class MaterialSkillTask implements Runnable {

		private MaterialSkill skill;
		private int secondsElapsed;

		@Override
		public void run() {
			if (secondsElapsed++ % (skill == null ? 1 : skill.getFrequency()) != 0)
				return;
			if (!isTouched)
				return;
			if (!creature.isSpawned() || creature.isDead())
				return;
			if (creature instanceof Player player && player.isProtectionActive())
				return;
			if ((skill = findFirstSkillWithMatchingCondition()) == null) // skip if currently nothing matches (fires are off while raining)
				return;
			if (GeoDataConfig.GEO_MATERIALS_SHOWDETAILS && creature instanceof Player player && player.isStaff())
				PacketSendUtility.sendMessage(player, AbstractMaterialSkillActor.this.getClass().getSimpleName() + " use skill=" + skill.getId());
			SkillEngine.getInstance().applyEffectDirectly(skill.getId(), skill.getSkillLevel(), creature, creature, null, Effect.ForceType.MATERIAL_SKILL);
		}
	}
}
