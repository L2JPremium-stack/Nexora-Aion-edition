package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.item.actions.TuningAction;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.services.item.ItemActionService;
import com.nexora.gameserver.utils.audit.AuditLogger;

/**
 * @author xTz
 */
public class CM_TUNE extends AionClientPacket {

	private int itemObjectId, tuningScrollObjectId;

	public CM_TUNE(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		itemObjectId = readD();
		tuningScrollObjectId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null)
			return;

		Item item = player.getInventory().getItemByObjId(itemObjectId);
		if (item == null)
			return;

		if (!item.isIdentified()) {
			ItemActionService.identifyItem(player, item);
		} else if (tuningScrollObjectId != 0) {
			Item tuningScroll = player.getInventory().getItemByObjId(tuningScrollObjectId);
			if (tuningScroll == null)
				return;

			TuningAction action = tuningScroll.getItemTemplate().getActions().getTuningAction();
			if (action != null && action.canAct(player, tuningScroll, item))
				action.act(player, tuningScroll, item);
		} else {
			AuditLogger.log(player, "attempted to tune an already identified item without tuning scroll.");
		}
	}

}
