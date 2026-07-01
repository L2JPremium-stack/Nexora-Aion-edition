package com.nexora.gameserver.network.aion.clientpackets;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexora.gameserver.model.TaskId;
import com.nexora.gameserver.model.gameobjects.LetterType;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.network.aion.AionClientPacket;
import com.nexora.gameserver.network.aion.AionConnection.State;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.spawnengine.VisibleObjectSpawner;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.ThreadPoolManager;

/**
 * @author antness thx to Guapo for sniffing
 */
public class CM_READ_EXPRESS_MAIL extends AionClientPacket {

	private static final Logger log = LoggerFactory.getLogger(CM_READ_EXPRESS_MAIL.class);
	private byte action;

	public CM_READ_EXPRESS_MAIL(int opcode, Set<State> validStates) {
		super(opcode, validStates);
	}

	@Override
	protected void readImpl() {
		action = readC();
	}

	@Override
	protected void runImpl() {
		final Player player = getConnection().getActivePlayer();

		switch (action) {
			case 0: // window is closed
				if (player.getPostman() != null) {
					player.getPostman().getController().delete();
					player.setPostman(null);
				}
				break;
			case 1: // click on icon
				boolean haveUnreadExpress = player.getMailbox().haveUnreadByType(LetterType.EXPRESS);
				boolean haveUnreadBlackcloud = player.getMailbox().haveUnreadByType(LetterType.BLACKCLOUD);
				if (player.getPostman() != null) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_POSTMAN_ALREADY_SUMMONED());
				} else if (player.isFlying()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_POSTMAN_UNABLE_IN_FLIGHT());
				} else if (haveUnreadBlackcloud) {
					VisibleObjectSpawner.spawnPostman(player);
				} else if (haveUnreadExpress) {
					if (player.getController().hasTask(TaskId.EXPRESS_MAIL_USE)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_POSTMAN_UNABLE_IN_COOLTIME());
						return;
					}
					VisibleObjectSpawner.spawnPostman(player);
					player.getController().addTask(TaskId.EXPRESS_MAIL_USE,
						ThreadPoolManager.getInstance().schedule(() -> player.getController().cancelTask(TaskId.EXPRESS_MAIL_USE), 600000)); // 10 min
				}
				break;
			default:
				log.warn(player + " sent unknown read express mail action type: " + action);
		}
	}

}
