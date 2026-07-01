package quest.tiamat_stronghold;

import static com.nexora.gameserver.model.DialogAction.*;

import com.nexora.commons.utils.Rnd;
import com.nexora.gameserver.geoEngine.math.Vector3f;
import com.nexora.gameserver.model.gameobjects.Item;
import com.nexora.gameserver.model.gameobjects.Npc;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.spawns.SpawnTemplate;
import com.nexora.gameserver.questEngine.handlers.AbstractQuestHandler;
import com.nexora.gameserver.questEngine.handlers.HandlerResult;
import com.nexora.gameserver.questEngine.model.QuestEnv;
import com.nexora.gameserver.questEngine.model.QuestState;
import com.nexora.gameserver.questEngine.model.QuestStatus;
import com.nexora.gameserver.spawnengine.SpawnEngine;
import com.nexora.gameserver.utils.ThreadPoolManager;
import com.nexora.gameserver.world.WorldPosition;
import com.nexora.gameserver.world.geo.GeoService;
import com.nexora.gameserver.world.zone.ZoneName;

/**
 * @author Estrayl
 */
public class _30721OminousDebrisEnergy extends AbstractQuestHandler {

	private static final int QUEST_ITEM_ID = 182215698; // Sedative
	private static final int START_NPC_ID = 804704; // Eukraton
	private static final int TALK_NPC_1_ID = 804870; // Monroe
	private static final int TALK_NPC_2_ID = 804868; // Rosalee
	private static final int ATTACKER_NPC_ID = 217424; // Mirror Image

	public _30721OminousDebrisEnergy() {
				super(30721);
		}

	@Override
	public void register() {
		qe.registerQuestNpc(START_NPC_ID).addOnQuestStart(questId);
		qe.registerQuestNpc(TALK_NPC_1_ID).addOnTalkEvent(questId);
		qe.registerQuestNpc(TALK_NPC_2_ID).addOnTalkEvent(questId);
		qe.registerQuestItem(QUEST_ITEM_ID, questId);
	}

	public boolean onDialogEvent(QuestEnv env) {
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		int dialogActionId = env.getDialogActionId();
		int targetId = env.getTargetId();

		if (qs == null || qs.isStartable()) {
			if (targetId == START_NPC_ID) {
				if (dialogActionId == QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == TALK_NPC_1_ID && var == 0) {
				if (dialogActionId == QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				} else if (dialogActionId == SETPRO1) {
					changeQuestStep(env, 0, 1);
					return closeDialogWindow(env);
				}
			} else if (targetId == TALK_NPC_2_ID) {
				if (dialogActionId == QUEST_SELECT) {
					if (var == 1) {
						return sendQuestDialog(env, 1352);
					} else if (var == 3) {
						return sendQuestDialog(env, 2034);
					}
				} else if (dialogActionId == SETPRO2) {
					changeQuestStep(env, 1, 2);
					giveQuestItem(env, QUEST_ITEM_ID, 1);
					return closeDialogWindow(env);
				} else if (dialogActionId == SET_SUCCEED) {
					changeQuestStep(env, 3, 4, true);
					return closeDialogWindow(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == TALK_NPC_1_ID) {
				if (dialogActionId == QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (player.isInsideItemUseZone(ZoneName.get("LF5_ITEMUSEAREA_Q30721"))) {
				int var = qs.getQuestVarById(0);
				if (var == 2) {
					boolean isItemUseComplete = useQuestItem(env, item, 2, 3, false);
					if (isItemUseComplete) {
						ThreadPoolManager.getInstance().schedule(() -> {
							rndSpawnInRange(player, Rnd.nextFloat(4f));
							rndSpawnInRange(player, Rnd.nextFloat(4f));
						}, 1500);
					}
					return HandlerResult.fromBoolean(isItemUseComplete);
				}
			}
		}
		return HandlerResult.FAILED;
	}

	private void rndSpawnInRange(Player player, float distance) {
		WorldPosition p = player.getPosition();
		double angleRadians = Math.toRadians(Rnd.nextFloat(360f));
		float x = p.getX() + (float) (Math.cos(angleRadians) * distance);
		float y = p.getY() + (float) (Math.sin(angleRadians) * distance);

		Vector3f pos = GeoService.getInstance().getClosestCollision(player, x, y, p.getZ());
		SpawnTemplate template = SpawnEngine.newSingleTimeSpawn(p.getMapId(), ATTACKER_NPC_ID, pos.getX(), pos.getY(), pos.getZ(), (byte) 0);

		Npc npc = (Npc) SpawnEngine.spawnObject(template, p.getInstanceId());
		npc.getAggroList().addHate(player, 1000);
	}
}
