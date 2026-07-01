package com.nexora.loginserver;

import org.slf4j.LoggerFactory;

import com.nexora.commons.database.DatabaseFactory;
import com.nexora.commons.logging.Logging;
import com.nexora.commons.utils.concurrent.UncaughtExceptionHandler;
import com.nexora.commons.utils.info.SystemInfo;
import com.nexora.commons.utils.info.VersionInfo;
import com.nexora.loginserver.configs.Config;
import com.nexora.loginserver.controller.BannedIpController;
import com.nexora.loginserver.dao.BannedHddDAO;
import com.nexora.loginserver.dao.BannedMacDAO;
import com.nexora.loginserver.network.NetConnector;
import com.nexora.loginserver.network.ncrypt.KeyGen;
import com.nexora.loginserver.service.PlayerTransferService;

import ch.qos.logback.classic.LoggerContext;

/**
 * @author -Nemesiss-
 */
class LoginServer {

	void main() {
		Logging.init(); // must run before instantiating any logger
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());

		Config.load();
		DatabaseFactory.init();
		KeyGen.init();

		GameServerTable.load();
		BannedIpController.start();
		BannedMacDAO.cleanExpiredBans();
		BannedHddDAO.cleanExpiredBans();

		PlayerTransferService.getInstance();

		VersionInfo.logAll(LoginServer.class);
		SystemInfo.logAll();

		NetConnector.connect();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
	}

	private static class ShutdownHook extends Thread {

		@Override
		public void run() {
			PlayerTransferService.getInstance().shutdown();
			NetConnector.shutdown();
			// shut down logger factory to flush all pending log messages
			((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
		}
	}
}
