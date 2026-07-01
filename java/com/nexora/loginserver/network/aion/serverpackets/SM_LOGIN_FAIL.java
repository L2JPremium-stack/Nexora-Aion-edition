package com.nexora.loginserver.network.aion.serverpackets;

import com.nexora.loginserver.network.aion.AionAuthResponse;
import com.nexora.loginserver.network.aion.AionServerPacket;
import com.nexora.loginserver.network.aion.LoginConnection;

/**
 * @author KID
 */
public class SM_LOGIN_FAIL extends AionServerPacket {

	/**
	 * response - why login fail
	 */
	private AionAuthResponse response;

	/**
	 * Constructs new instance of <tt>SM_LOGIN_FAIL</tt> packet.
	 * 
	 * @param response
	 *          auth responce
	 */
	public SM_LOGIN_FAIL(AionAuthResponse response) {
		super(0x01);
		this.response = response;
	}

	@Override
	protected void writeImpl(LoginConnection con) {
		writeD(response.getId());
	}
}
