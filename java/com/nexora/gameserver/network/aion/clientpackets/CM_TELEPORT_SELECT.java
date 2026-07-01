package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import com.nexora.gameserver.model.animations.TeleportAnimation;
import com.nexora.gameserver.model.gameobjects.AionObject;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.teleport.TeleportLocation;
import com.nexora.gameserver.model.templates.teleport.TeleporterTemplate;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.audit.AuditLogger;
import com.nexora.gameserver.world.World;

/**
 * @author ATracer, orz, KID
 */
public class CM_TELEPORT_SELECT extends AionClientPacket {

	/**
	 * NPC object ID
	 */
	private int targetObjId;

	/**
	 * Destination of teleport
	 */
	private int locId;

	public CM_TELEPORT_SELECT(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		targetObjId = readD();
		locId = readD(); // locationId
		readH();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.isDead())
			return;

		AionObject obj = player.getKnownList().getObject(targetObjId);
		if (!(obj instanceof Npc npc)) {
			if (obj == null)
				obj = World.getInstance().findVisibleObject(targetObjId);
			AuditLogger.log(player, "tried to teleport to locId " + locId + " via " + (obj == null ? "unknown npc (objId " + targetObjId + ")" : obj)
				+ " at " + player.getPosition());
			return;
		}
		TeleporterTemplate template = TeleportService.validateTeleporterAndGetTemplate(player, npc);
		if (template == null)
			return;
		TeleportLocation location = template.getTeleLocIdData().getTeleportLocation(locId);
		if (location == null) {
			AuditLogger.log(player, "tried to teleport to invalid locId " + locId + " via " + npc + " at " + player.getPosition());
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE());
			return;
		}
		TeleportService.teleport(player, location, npc.hasStatic() ? TeleportAnimation.JUMP_IN_STATUE : TeleportAnimation.JUMP_IN);
	}
}
