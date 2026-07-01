package com.nexora.gameserver.controllers.observer;

import com.nexora.gameserver.dataholders.DataManager;
import com.nexora.gameserver.geoEngine.math.Vector3f;
import com.nexora.gameserver.model.Race;
import com.nexora.gameserver.model.flyring.FlyRing;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.questEngine.QuestEngine;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.skillengine.model.SkillTemplate;

/**
 * @author xavier, Source
 */
public class FlyRingObserver extends ActionObserver {

	private final Player player;
	private final FlyRing ring;
	private Vector3f oldPosition;

	public FlyRingObserver(FlyRing ring, Player player) {
		super(ObserverType.MOVE);
		this.player = player;
		this.ring = ring;
		this.oldPosition = new Vector3f(player.getX(), player.getY(), player.getZ());
	}

	@Override
	public void moved() {
		Vector3f newPosition = new Vector3f(player.getX(), player.getY(), player.getZ());
		if (ring.isCrossed(oldPosition, newPosition)) {
			if (ring.getTemplate().getMap() == 400010000 || isQuestactive() || isInstancetactive()) {
				SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(265); // Wings of Aether
				Effect speedUp = new Effect(player, player, skillTemplate, skillTemplate.getLvl());
				speedUp.initialize();
				speedUp.addAllEffectToSucess();
				speedUp.applyEffect();
			}
			QuestEngine.getInstance().onPassFlyingRing(new QuestEnv(null, player, 0), ring.getName());
		}
		oldPosition = newPosition;
	}

	private boolean isInstancetactive() {
		return ring.getPosition().getWorldMapInstance().getInstanceHandler().onPassFlyingRing(player, ring.getName());
	}

	private boolean isQuestactive() {
		int questId = player.getRace() == Race.ASMODIANS ? 2042 : 1044;
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null)
			return false;

		return qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) >= 2 && qs.getQuestVarById(0) <= 8;
	}

}
