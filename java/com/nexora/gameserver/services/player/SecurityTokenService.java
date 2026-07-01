package com.nexora.gameserver.services.player;

import java.security.SecureRandom;
import java.util.Base64;

import com.nexora.gameserver.model.account.Account;

/**
 * @author Artur
 */
public class SecurityTokenService {

	private SecurityTokenService() {
	}

	public static void generateToken(Account account) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] token = new byte[16];
		secureRandom.nextBytes(token);
		account.setSecurityToken(Base64.getEncoder().encodeToString(token));
	}
}
