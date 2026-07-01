package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.VisibleObject;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;

/**
 * @author alexa026, Avol, ATracer, KID
 */
public class CM_ATTACK extends AionClientPacket {

	private static final Logger log = LoggerFactory.getLogger(CM_ATTACK.class);
	/**
	 * Target object id that client wants to TALK WITH or 0 if wants to unselect
	 */
	private int targetObjectId;
	// TODO: Question, are they really needed?
	@SuppressWarnings("unused")
	private int attackno;

	private int time;
	@SuppressWarnings("unused")
	private int type;

	public CM_ATTACK(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		targetObjectId = readD();// empty
		attackno = readUC();// empty
		time = readUH();// empty
		type = readUC();// empty
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.isDead())
			return;

		if (player.isProtectionActive())
			player.getController().stopProtectionActiveTask();

		VisibleObject obj = player.getKnownList().getObject(targetObjectId);
		if (obj instanceof Creature) {
			player.getController().attackTarget((Creature) obj, time, false);
		} else if (obj != null) {
			log.warn(player + " attacking unsupported target " + obj);
		}
	}
}
