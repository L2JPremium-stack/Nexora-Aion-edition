package com.nexora.gameserver.model.gameobjects.player.title;

import com.nexora.gameserver.model.Expirable;
import com.nexora.gameserver.model.gameobjects.player.Player;
import com.nexora.gameserver.model.templates.TitleTemplate;
import com.nexora.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.nexora.gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke
 */
public class Title implements Expirable {

	private TitleTemplate template;
	private int id;
	private int expireTime;

	/**
	 * @param template
	 * @param id
	 * @param expireTime
	 */
	public Title(TitleTemplate template, int id, int expireTime) {
		this.template = template;
		this.id = id;
		this.expireTime = expireTime;
	}

	/**
	 * @return Returns the template.
	 */
	public TitleTemplate getTemplate() {
		return template;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	@Override
	public int getExpireTime() {
		return expireTime;
	}

	@Override
	public void onExpire(Player player) {
		player.getTitleList().removeTitle(id);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DELETE_CASH_TITLE_BY_TIMEOUT(template.getL10n()));
	}

}
