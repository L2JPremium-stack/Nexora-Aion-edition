package com.nexora.loginserver.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nexora.commons.network.NioServer;
import com.nexora.commons.network.ServerCfg;
import com.nexora.loginserver.configs.Config;
import com.nexora.loginserver.network.aion.LoginConnection;
import com.nexora.loginserver.network.gameserver.GsConnection;

/**
 * @author KID, Neon
 */
public class NetConnector {

	private final static NioServer instance;
	private final static ExecutorService dcExecutor = Executors.newCachedThreadPool();

	static {
		ServerCfg aion = new ServerCfg(Config.CLIENT_SOCKET_ADDRESS, "Aion game clients", LoginConnection::new);
		ServerCfg gs = new ServerCfg(Config.GAMESERVER_SOCKET_ADDRESS, "game servers", GsConnection::new);
		instance = new NioServer(Config.NIO_READ_WRITE_THREADS, aion, gs);
	}

	public static void connect() {
		instance.connect(dcExecutor);
	}

	public static void shutdown() {
		instance.shutdown();
		dcExecutor.shutdown();
	}
}
