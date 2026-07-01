package com.nexora.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.nexora.gameserver.model.gameobjects.Creature;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.nexora.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.services.teleport.TeleportService;
import com.nexora.gameserver.skillengine.model.Effect;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Bio, Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecallInstantEffect")
public class RecallInstantEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		final Creature effector = effect.getEffector();
		final Player effected = (Player) effect.getEffected();

		/**
		 * TODO need to confirm if cannot be summoned while on abnormal effects stunned, sleeping, feared, etc.
		 */
		RequestResponseHandler<Creature> rrh = new RequestResponseHandler<Creature>(effector) {

			int worldId = effect.getWorldId();
			int instanceId = effect.getInstanceId();
			float locationX = effect.getSkill().getX();
			float locationY = effect.getSkill().getY();
			float locationZ = effect.getSkill().getZ();
			byte locationH = effect.getSkill().getH();

			@Override
			public void denyRequest(Creature effector, Player effected) {
				PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.STR_MSG_Recall_Rejected_EFFECT(effected.getName()));
				PacketSendUtility.sendPacket(effected, SM_SYSTEM_MESSAGE.STR_MSG_Recall_Rejected_EFFECT(effector.getName()));
			}

			@Override
			public void acceptRequest(Creature effector, Player effected) {
				TeleportService.teleportTo(effected, worldId, instanceId, locationX, locationY, locationZ, locationH);
			}
		};

		if (effected.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_SUMMON_PARTY_DO_YOU_ACCEPT_REQUEST, rrh))
			PacketSendUtility.sendPacket(effected,
				new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_SUMMON_PARTY_DO_YOU_ACCEPT_REQUEST, 0, 0, effector.getName(), "Summon Group Member", 30));
	}

	@Override
	public void calculate(Effect effect) {
		final Creature effector = effect.getEffector();

		if (!(effect.getEffected() instanceof Player))
			return;
		Player effected = (Player) effect.getEffected();

		if (effected.getController().isInCombat())
			return;

		if (effector.getWorldId() == effected.getWorldId() && !effector.isInInstance() && !(effector.isEnemy(effected))) {
			effect.getSkill().setTargetPosition(effector.getX(), effector.getY(), effector.getZ(), effector.getHeading());
			effect.addSuccessEffect(this);
		}
	}
}
