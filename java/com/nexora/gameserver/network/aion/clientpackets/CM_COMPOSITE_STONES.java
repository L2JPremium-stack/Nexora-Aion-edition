package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.item.actions.CompositionAction;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.restrictions.PlayerRestrictions;

/**
 * Created with IntelliJ IDEA. User: pixfid Date: 7/14/13 Time: 5:30 PM
 */
public class CM_COMPOSITE_STONES extends AionClientPacket {

	private int compinationToolItemObjectId;
	private int firstItemObjectId;
	private int secondItemObjectId;

	/**
	 * Constructs new client packet instance. ByBuffer and ClientConnection should be later set manually, after using this constructor.
	 *
	 * @param opcode
	 *          packet id
	 * @param state
	 *          connection valid state
	 * @param restStates
	 *          rest of connection valid state (optional - if there are more than one)
	 */
	public CM_COMPOSITE_STONES(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		compinationToolItemObjectId = readD();
		firstItemObjectId = readD();
		secondItemObjectId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null)
			return;

		if (player.isProtectionActive()) {
			player.getController().stopProtectionActiveTask();
		}

		if (player.isCasting()) {
			player.getController().cancelCurrentSkill(null);
		}

		Item tools = player.getInventory().getItemByObjId(compinationToolItemObjectId);
		if (tools == null)
			return;
		Item first = player.getInventory().getItemByObjId(firstItemObjectId);
		if (first == null)
			return;
		Item second = player.getInventory().getItemByObjId(secondItemObjectId);
		if (second == null)
			return;

		if (!PlayerRestrictions.canUseItem(player, tools))
			return;

		CompositionAction action = new CompositionAction();

		if (!action.canAct(player, tools, first, second))
			return;

		action.act(player, tools, first, second);
	}
}
