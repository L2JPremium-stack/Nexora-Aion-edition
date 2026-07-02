package com.aionemu.dashboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

final class ServerProcess
{
	
	interface Listener
	{
		void onLog(ServerProcess process, String line);
		
		void onStateChanged(ServerProcess process);
	}
	
	private static final Charset CONSOLE_CHARSET = Charset.forName("CP850");
	
	private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();
	private volatile ServerDefinition definition;
	private volatile Process process;
	private volatile Instant startedAt;
	
	ServerProcess(ServerDefinition definition)
	{
		this.definition = definition;
	}
	
	void addListener(Listener listener)
	{
		listeners.addIfAbsent(listener);
	}
	
	ServerDefinition definition()
	{
		return definition;
	}
	
	void updateDefinition(ServerDefinition definition)
	{
		this.definition = definition;
		fireStateChanged();
	}
	
	synchronized void start() throws IOException
	{
		if (isRunning())
			return;
		ServerDefinition current = definition;
		List<String> command = command(current);
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(current.workingDirectory().toFile());
		builder.redirectErrorStream(true);
		appendDashboardLog("Starting " + current.title() + "...");
		process = builder.start();
		startedAt = Instant.now();
		startReader(process);
		startWatcher(process);
		fireStateChanged();
	}
	
	void stop()
	{
		Thread thread = new Thread(this::stopBlocking, "nexora-stop-" + definition.id());
		thread.setDaemon(true);
		thread.start();
	}
	
	void stopAndWait()
	{
		stopBlocking();
	}
	
	void restart()
	{
		Thread thread = new Thread(() -> {
			stopBlocking();
			try
			{
				start();
			}
			catch (IOException e)
			{
				appendDashboardLog("Could not restart " + definition.title() + ": " + e.getMessage());
			}
		}, "nexora-restart-" + definition.id());
		thread.setDaemon(true);
		thread.start();
	}
	
	boolean isRunning()
	{
		Process current = process;
		return current != null && current.isAlive();
	}
	
	Duration uptime()
	{
		Instant start = startedAt;
		return start == null || !isRunning() ? Duration.ZERO : Duration.between(start, Instant.now());
	}
	
	long pid()
	{
		Process current = process;
		return current == null ? 0 : current.pid();
	}
	
	long memoryMb()
	{
		return Utils.processTreeMemoryMb(pid());
	}
	
	void openFolder() throws IOException
	{
		Utils.openFolder(definition.workingDirectory());
	}
	
	void openConsole() throws IOException
	{
		Utils.openConsole(definition.workingDirectory(), definition.logFile(), definition.title());
	}
	
	private synchronized void stopBlocking()
	{
		Process current = process;
		if (current == null || !current.isAlive())
			return;
		appendDashboardLog("Stopping " + definition.title() + "...");
		destroyProcessTree(current);
		try
		{
			if (!current.waitFor(5, TimeUnit.SECONDS))
			{
				appendDashboardLog("Forcing " + definition.title() + " shutdown...");
				destroyProcessTree(current);
				current.destroyForcibly();
				current.waitFor(5, TimeUnit.SECONDS);
			}
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		if (process == current)
		{
			process = null;
			startedAt = null;
		}
		fireStateChanged();
	}
	
	private static List<String> command(ServerDefinition current)
	{
		if (Utils.isWindows() && Files.isRegularFile(current.scriptFile()))
			return List.of("cscript.exe", "//NoLogo", current.scriptFile().toString(), "--dashboard");

		List<String> command = new ArrayList<>();
		command.add(current.javaExecutable());
		command.add("-Xms" + current.xms());
		command.add("-Xmx" + current.xmx());
		command.addAll(current.extraJvmArgs());
		command.add("-cp");
		command.add(classpath(current));
		command.add(current.mainClass());
		return command;
	}
	
	private static String classpath(ServerDefinition current)
	{
		String separator = System.getProperty("path.separator");
		return "." + separator + "config" + separator + current.rootDirectory().resolve("libs").resolve("*");
	}

	private static void destroyProcessTree(Process current)
	{
		if (Utils.isWindows())
		{
			try
			{
				new ProcessBuilder("taskkill", "/PID", Long.toString(current.pid()), "/T", "/F").start().waitFor(5, TimeUnit.SECONDS);
				return;
			}
			catch (Exception e)
			{
				// Fall back to the Java process handle below.
			}
		}
		current.destroy();
	}
	
	private void startReader(Process watchedProcess)
	{
		Thread thread = new Thread(() -> {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(watchedProcess.getInputStream(), CONSOLE_CHARSET)))
			{
				String line;
				while ((line = reader.readLine()) != null)
				{
					appendServerLog(line);
				}
			}
			catch (IOException e)
			{
				if (watchedProcess.isAlive())
					appendDashboardLog("Log stream closed: " + e.getMessage());
			}
		}, "nexora-log-" + definition.id());
		thread.setDaemon(true);
		thread.start();
	}
	
	private void startWatcher(Process watchedProcess)
	{
		Thread thread = new Thread(() -> {
			try
			{
				int exitCode = watchedProcess.waitFor();
				appendDashboardLog(definition.title() + " finished with exit code " + exitCode + ".");
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
			finally
			{
				if (process == watchedProcess)
				{
					process = null;
					startedAt = null;
				}
				fireStateChanged();
			}
		}, "nexora-watch-" + definition.id());
		thread.setDaemon(true);
		thread.start();
	}
	
	private void appendServerLog(String line)
	{
		for (Listener listener : listeners)
			listener.onLog(this, line);
	}
	
	private void appendDashboardLog(String line)
	{
		appendServerLog("[dashboard] " + line);
	}
	
	private void fireStateChanged()
	{
		for (Listener listener : listeners)
			listener.onStateChanged(this);
	}
}
