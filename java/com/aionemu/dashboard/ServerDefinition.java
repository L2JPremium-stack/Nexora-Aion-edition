package com.aionemu.dashboard;

import java.awt.Color;
import java.nio.file.Path;
import java.util.List;

final class ServerDefinition {

	enum Kind {
		LOGIN,
		GAME
	}

	private final Kind kind;
	private final String id;
	private final String title;
	private final String subtitle;
	private final Path rootDirectory;
	private final Path workingDirectory;
	private final Path logFile;
	private final Path scriptFile;
	private final String javaExecutable;
	private final String mainClass;
	private final String xms;
	private final String xmx;
	private final List<String> extraJvmArgs;
	private final String port;
	private final Color accent;

	ServerDefinition(Kind kind, String id, String title, String subtitle, Path rootDirectory, Path workingDirectory, Path logFile, Path scriptFile,
		String javaExecutable, String mainClass, String xms, String xmx, List<String> extraJvmArgs, String port, Color accent) {
		this.kind = kind;
		this.id = id;
		this.title = title;
		this.subtitle = subtitle;
		this.rootDirectory = rootDirectory;
		this.workingDirectory = workingDirectory;
		this.logFile = logFile;
		this.scriptFile = scriptFile;
		this.javaExecutable = javaExecutable;
		this.mainClass = mainClass;
		this.xms = xms;
		this.xmx = xmx;
		this.extraJvmArgs = List.copyOf(extraJvmArgs);
		this.port = port;
		this.accent = accent;
	}

	Kind kind() {
		return kind;
	}

	String id() {
		return id;
	}

	String title() {
		return title;
	}

	String subtitle() {
		return subtitle;
	}

	Path rootDirectory() {
		return rootDirectory;
	}

	Path workingDirectory() {
		return workingDirectory;
	}

	Path logFile() {
		return logFile;
	}

	Path scriptFile() {
		return scriptFile;
	}

	String javaExecutable() {
		return javaExecutable;
	}

	String mainClass() {
		return mainClass;
	}

	String xms() {
		return xms;
	}

	String xmx() {
		return xmx;
	}

	List<String> extraJvmArgs() {
		return extraJvmArgs;
	}

	String port() {
		return port;
	}

	Color accent() {
		return accent;
	}

	int maxMemoryMb() {
		return Utils.memoryToMb(xmx);
	}
}
