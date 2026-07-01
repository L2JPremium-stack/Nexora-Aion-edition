package com.nexora.gameserver.world.zone.handler;

import java.lang.annotation.IncompleteAnnotationException;
import java.util.HashMap;
import java.util.Map;

import com.nexora.gameserver.controllers.observer.AbstractQuestZoneObserver;
import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.zone.ZoneTemplate;
import com.nexora.gameserver.world.zone.ZoneInstance;

/**
 * @author Rolandas
 */
public abstract class QuestZoneHandler extends GeneralZoneHandler {

	protected final Map<Integer, AbstractQuestZoneObserver> observed = new HashMap<>();
	protected final int questId;

	public QuestZoneHandler() {
		ZoneNameAnnotation annotation = getClass().getAnnotation(ZoneNameAnnotation.class);
		if (annotation == null || annotation.questId() == 0 || DataManager.QUEST_DATA.getQuestById(annotation.questId()) == null)
			throw new IncompleteAnnotationException(ZoneNameAnnotation.class, "questId");
		questId = annotation.questId();
	}

	@Override
	public void onEnterZone(Creature creature, ZoneInstance zone) {
		if (!(creature instanceof Player))
			return;
		AbstractQuestZoneObserver observer = createObserver((Player) creature, zone.getZoneTemplate());
		creature.getObserveController().addObserver(observer);
		observed.put(creature.getObjectId(), observer);
	}

	@Override
	public void onLeaveZone(Creature creature, ZoneInstance zone) {
		if (!(creature instanceof Player))
			return;
		AbstractQuestZoneObserver observer = observed.get(creature.getObjectId());
		if (observer != null) {
			creature.getObserveController().removeObserver(observer);
			observed.remove(creature.getObjectId());
		}
	}

	public abstract AbstractQuestZoneObserver createObserver(Player player, ZoneTemplate zoneTemplate);

}
