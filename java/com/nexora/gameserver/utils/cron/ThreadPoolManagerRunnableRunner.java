package com.nexora.gameserver.utils.cron;

import com.nexora.gameserver.services.cron.RunnableRunner;
import com.nexora.gameserver.utils.ThreadPoolManager;

public class ThreadPoolManagerRunnableRunner extends RunnableRunner {

	@Override
	public void executeRunnable(Runnable r) {
		ThreadPoolManager.getInstance().execute(r);
	}

	@Override
	public void executeLongRunningRunnable(Runnable r) {
		ThreadPoolManager.getInstance().executeLongRunning(r);
	}
}
