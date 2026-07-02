package com.aionemu.dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public final class ConfigManager
{
	
	private static final String DASHBOARD_CONFIG = "dashboard.properties";
	
	private final Path rootDirectory;
	private final Path dashboardDirectory;
	private final Path dashboardConfigFile;
	private final Properties settings = new Properties();
	
	ConfigManager()
	{
		rootDirectory = locateRoot(Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize());
		dashboardDirectory = rootDirectory.resolve("dashboard");
		dashboardConfigFile = dashboardDirectory.resolve(DASHBOARD_CONFIG);
		load();
	}
	
	Path rootDirectory()
	{
		return rootDirectory;
	}
	
	Path dashboardDirectory()
	{
		return dashboardDirectory;
	}
	
	String version()
	{
		return value("dashboard.version", "20.0");
	}
	
	String javaExecutable()
	{
		return value("java.executable", "java");
	}
	
	String loginXms()
	{
		return value("login.xms", "48m");
	}
	
	String loginXmx()
	{
		return value("login.xmx", "48m");
	}
	
	String gameXms()
	{
		return value("game.xms", "1024m");
	}
	
	String gameXmx()
	{
		return value("game.xmx", "2560m");
	}
	
	ServerDefinition createLoginDefinition()
	{
		Path serverDir = rootDirectory.resolve("login-server");
		String port = readPort(serverDir.resolve("config/network/network.properties"), "loginserver.network.gameserver.socket_address", "9014");
		return new ServerDefinition(ServerDefinition.Kind.LOGIN, "login", "LOGIN SERVER", "AION_LOGIN", rootDirectory, serverDir, serverDir.resolve("log/server_console.log"), serverDir.resolve("start.vbs"), javaExecutable(), value("login.mainClass", "com.aionemu.loginserver.LoginServer"), loginXms(), loginXmx(), Utils.splitCommandLine(value("login.extraJvmArgs", "-XX:+UseNUMA -DconsoleEncoding=CP850")), port, AppTheme.PURPLE_BRIGHT);
	}
	
	ServerDefinition createGameDefinition()
	{
		Path serverDir = rootDirectory.resolve("game-server");
		String port = readPort(serverDir.resolve("config/network/network.properties"), "gameserver.network.client.socket_address", "7777");
		return new ServerDefinition(ServerDefinition.Kind.GAME, "game", "GAME SERVER", "AION_AION", rootDirectory, serverDir, serverDir.resolve("log/server_console.log"), serverDir.resolve("start.vbs"), javaExecutable(), value("game.mainClass", "com.aionemu.gameserver.GameServer"), gameXms(), gameXmx(), Utils.splitCommandLine(value("game.extraJvmArgs", "-XX:+UseNUMA -XX:+UseCompactObjectHeaders -DconsoleEncoding=CP850")), port, AppTheme.BLUE);
	}
	
	void saveRuntimeSettings(String javaExecutable, String loginXms, String loginXmx, String gameXms, String gameXmx) throws IOException
	{
		settings.setProperty("java.executable", trimOr(javaExecutable, "java"));
		settings.setProperty("login.xms", trimOr(loginXms, "48m"));
		settings.setProperty("login.xmx", trimOr(loginXmx, "48m"));
		settings.setProperty("game.xms", trimOr(gameXms, "1024m"));
		settings.setProperty("game.xmx", trimOr(gameXmx, "2560m"));
		save();
	}
	
	List<String> settingRows()
	{
		return List.of("Root: " + rootDirectory, "Dashboard: " + dashboardDirectory, "Java: " + javaExecutable(), "Login Xmx: " + loginXmx(), "Game Xmx: " + gameXmx());
	}
	
	private void load()
	{
		settings.clear();
		settings.putAll(defaults());
		if (!Files.isRegularFile(dashboardConfigFile))
			return;
		try (InputStream in = Files.newInputStream(dashboardConfigFile))
		{
			settings.load(in);
		}
		catch (IOException e)
		{
			System.err.println("Could not load dashboard settings: " + e.getMessage());
		}
	}
	
	private void save() throws IOException
	{
		Files.createDirectories(dashboardDirectory);
		try (OutputStream out = Files.newOutputStream(dashboardConfigFile))
		{
			settings.store(out, "Nexora dashboard runtime settings");
		}
	}
	
	private static Properties defaults()
	{
		Properties defaults = new Properties();
		defaults.setProperty("dashboard.version", "20.0");
		defaults.setProperty("java.executable", "java");
		defaults.setProperty("login.mainClass", "com.aionemu.loginserver.LoginServer");
		defaults.setProperty("login.xms", "48m");
		defaults.setProperty("login.xmx", "48m");
		defaults.setProperty("login.extraJvmArgs", "-XX:+UseNUMA -DconsoleEncoding=CP850");
		defaults.setProperty("game.mainClass", "com.aionemu.gameserver.GameServer");
		defaults.setProperty("game.xms", "1024m");
		defaults.setProperty("game.xmx", "2560m");
		defaults.setProperty("game.extraJvmArgs", "-XX:+UseNUMA -XX:+UseCompactObjectHeaders -DconsoleEncoding=CP850");
		return defaults;
	}
	
	private String value(String key, String fallback)
	{
		return settings.getProperty(key, fallback).trim();
	}
	
	private static String trimOr(String value, String fallback)
	{
		String trimmed = value == null ? "" : value.trim();
		return trimmed.isEmpty() ? fallback : trimmed;
	}
	
	private static String readPort(Path propertiesFile, String propertyName, String fallback)
	{
		if (!Files.isRegularFile(propertiesFile))
			return fallback;
		Properties properties = new Properties();
		try (InputStream in = Files.newInputStream(propertiesFile))
		{
			properties.load(in);
			return Utils.extractPort(properties.getProperty(propertyName), fallback);
		}
		catch (IOException e)
		{
			return fallback;
		}
	}
	
	private static Path locateRoot(Path start)
	{
		Path current = Files.isRegularFile(start) ? start.getParent() : start;
		while (current != null)
		{
			if (Files.isDirectory(current.resolve("login-server")) && Files.isDirectory(current.resolve("game-server")) && Files.isDirectory(current.resolve("libs")))
				return current;
			current = current.getParent();
		}
		return start;
	}
}
