package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;

/**
 * @author Rolandas
 */
public class CM_RECIPE_DELETE extends AionClientPacket {

	int recipeId;

	public CM_RECIPE_DELETE(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		recipeId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		player.getRecipeList().deleteRecipe(player, recipeId);
	}

}
