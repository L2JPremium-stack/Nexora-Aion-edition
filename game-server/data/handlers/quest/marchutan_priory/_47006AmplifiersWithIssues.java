package quest.marchutan_priory;

import static com.nexora.gameserver.model.DialogAction.*;
import static com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE.STR_MSG_DailyQuest_Ask_Mentor;

import com.nexora.gameserver.configs.main.GroupConfig;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.team.group.PlayerGroup;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.services.QuestService;
import com.nexora.gameserver.utils.PacketSendUtility;
import com.nexora.gameserver.utils.PositionUtil;

/**
 * @author Cheatkiller
 */
public class _47006AmplifiersWithIssues extends AbstractQuestHandler {

	public _47006AmplifiersWithIssues() {
		super(47006);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(700972).addOnTalkEvent(questId);
		qe.registerQuestNpc(799872).addOnTalkEvent(questId);
		qe.registerQuestNpc(217175).addOnKillEvent(questId);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 217175, 0, 5);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int dialogActionId = env.getDialogActionId();
		int targetId = env.getTargetId();

		if (qs == null || qs.isStartable()) {
			if (targetId == 0) {
				if (dialogActionId == QUEST_ACCEPT_1) {
					QuestService.startQuest(env);
					return closeDialogWindow(env);
				}
			}
		}

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 700972) {
				if (player.isInGroup()) {
					PlayerGroup group = player.getPlayerGroup();
					if (group.getMembers().stream().anyMatch(member -> member.isMentor() && PositionUtil.isInRange(player, member, GroupConfig.GROUP_MAX_DISTANCE))) {
						Npc npc = (Npc) env.getVisibleObject();
						npc.getController().deleteAndScheduleRespawn();
						spawnForFiveMinutes(217175, npc.getPosition());
						return true;
					}
					PacketSendUtility.sendPacket(player, STR_MSG_DailyQuest_Ask_Mentor());
				}
			}
			if (targetId == 799872) {
				if (dialogActionId == QUEST_SELECT) {
					if (qs.getQuestVarById(0) == 5) {
						return sendQuestDialog(env, 1352);
					}
				} else if (dialogActionId == SELECT_QUEST_REWARD) {
					return defaultCloseDialog(env, 5, 5, true, true);
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799872) {
				if (dialogActionId == USE_OBJECT) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
