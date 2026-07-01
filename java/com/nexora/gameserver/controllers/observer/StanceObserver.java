package com.nexora.gameserver.controllers.observer;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.item.actions.ItemActions;
import com.nexora.gameserver.skillengine.effect.AbnormalState;
import com.nexora.gameserver.skillengine.model.Skill;

/**
 * Watches all conditions when a stance needs to be removed
 * 
 * @author Neon
 */
public class StanceObserver extends ActionObserver {

	private final Player player;
	private final int stanceSkillId;

	public StanceObserver(Player player, int stanceSkillId) {
		super(ObserverType.ALL);
		this.player = player;
		this.stanceSkillId = stanceSkillId;
	}

	public int getStanceSkillId() {
		return stanceSkillId;
	}

	@Override
	public void startSkillCast(Skill skill) {
		String stack = skill.getSkillTemplate().getStack();
		if (!stack.startsWith("ITEM_") && !stack.startsWith("REMEDY_") && !stack.startsWith("POTION_")) // pots and scrolls don't stop stance
			player.getController().stopStance();
	}

	@Override
	public void itemused(Item item) {
		ItemActions actions = item.getItemTemplate().getActions();
		if (actions != null && actions.getSkillUseAction() == null) // skill actions are checked in startSkillCast, here we stop on RideAction etc.
			player.getController().stopStance();
	}

	@Override
	public void abnormalsetted(AbnormalState state) {
		if ((state.getId() & AbnormalState.STANCE_OFF.getId()) != 0)
			player.getController().stopStance();
	}
}
