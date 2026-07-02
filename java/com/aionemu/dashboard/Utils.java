package com.aionemu.dashboard;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class Utils {

	private Utils() {
	}

	static boolean isWindows() {
		return System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win");
	}

	static List<String> splitCommandLine(String value) {
		List<String> result = new ArrayList<>();
		if (value == null || value.isBlank())
			return result;
		StringBuilder token = new StringBuilder();
		boolean quoted = false;
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c == '"') {
				quoted = !quoted;
				continue;
			}
			if (Character.isWhitespace(c) && !quoted) {
				if (token.length() > 0) {
					result.add(token.toString());
					token.setLength(0);
				}
				continue;
			}
			token.append(c);
		}
		if (token.length() > 0)
			result.add(token.toString());
		return result;
	}

	static String extractPort(String endpoint, String fallback) {
		if (endpoint == null || endpoint.isBlank())
			return fallback;
		String value = endpoint.trim();
		int colon = value.lastIndexOf(':');
		return colon >= 0 && colon + 1 < value.length() ? value.substring(colon + 1).trim() : value;
	}

	static int memoryToMb(String value) {
		if (value == null || value.isBlank())
			return 0;
		String normalized = value.trim().toLowerCase(Locale.ROOT);
		int multiplier = 1;
		if (normalized.endsWith("g")) {
			multiplier = 1024;
			normalized = normalized.substring(0, normalized.length() - 1);
		} else if (normalized.endsWith("m")) {
			normalized = normalized.substring(0, normalized.length() - 1);
		}
		try {
			return Math.max(0, Integer.parseInt(normalized) * multiplier);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	static String formatDuration(Duration duration) {
		if (duration == null || duration.isNegative() || duration.isZero())
			return "00:00:00";
		long seconds = duration.toSeconds();
		long hours = seconds / 3600;
		long minutes = (seconds % 3600) / 60;
		long secs = seconds % 60;
		return String.format(Locale.ROOT, "%02d:%02d:%02d", hours, minutes, secs);
	}

	static String formatMb(long megabytes) {
		if (megabytes >= 1024)
			return String.format(Locale.ROOT, "%.1f GB", megabytes / 1024.0);
		return megabytes + " MB";
	}

	static void openFolder(Path folder) throws IOException {
		if (Desktop.isDesktopSupported() && Files.isDirectory(folder)) {
			Desktop.getDesktop().open(folder.toFile());
			return;
		}
		if (isWindows())
			new ProcessBuilder("explorer.exe", folder.toString()).start();
	}

	static void openConsole(Path folder, Path logFile, String title) throws IOException {
		if (!isWindows()) {
			openFolder(folder);
			return;
		}
		String command;
		if (Files.isRegularFile(logFile)) {
			command = "cd /d \"" + folder + "\" && powershell -NoExit -Command \"Get-Content -Path 'log\\server_console.log' -Tail 100 -Wait\"";
		} else {
			command = "cd /d \"" + folder + "\"";
		}
		new ProcessBuilder("cmd", "/c", "start", "Nexora " + title, "cmd", "/k", command).start();
	}

	static long processMemoryMb(long pid) {
		if (!isWindows() || pid <= 0)
			return 0;
		try {
			Process process = new ProcessBuilder("tasklist", "/FI", "PID eq " + pid, "/FO", "CSV", "/NH").start();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()))) {
				String line = reader.readLine();
				if (line == null || line.startsWith("INFO:"))
					return 0;
				String[] parts = line.split("\",\"");
				if (parts.length < 5)
					return 0;
				String memory = parts[4].replace("\"", "").replaceAll("[^0-9]", "");
				if (memory.isBlank())
					return 0;
				return Long.parseLong(memory) / 1024;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	static long processTreeMemoryMb(long pid) {
		if (!isWindows() || pid <= 0)
			return processMemoryMb(pid);
		String script = "$root=" + pid + ";"
			+ "$procs=Get-CimInstance Win32_Process | Select-Object ProcessId,ParentProcessId;"
			+ "$ids=@([int]$root);"
			+ "$changed=$true;"
			+ "while($changed){$changed=$false;foreach($p in $procs){"
			+ "if(($ids -contains [int]$p.ParentProcessId) -and -not ($ids -contains [int]$p.ProcessId)){"
			+ "$ids += [int]$p.ProcessId;$changed=$true}}};"
			+ "$sum=(Get-Process -Id $ids -ErrorAction SilentlyContinue | Measure-Object WorkingSet64 -Sum).Sum;"
			+ "if($sum){[math]::Round($sum/1MB)}else{0}";
		try {
			Process process = new ProcessBuilder("powershell", "-NoProfile", "-ExecutionPolicy", "Bypass", "-Command", script).start();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()))) {
				String line = reader.readLine();
				if (line == null || line.isBlank())
					return processMemoryMb(pid);
				return Long.parseLong(line.trim());
			}
		} catch (Exception e) {
			return processMemoryMb(pid);
		}
	}

	static long totalMemoryMb() {
		Object bean = ManagementFactory.getOperatingSystemMXBean();
		long total = invokeLong(bean, "getTotalMemorySize", -1);
		if (total < 0)
			total = invokeLong(bean, "getTotalPhysicalMemorySize", -1);
		if (total > 0)
			return total / 1024 / 1024;
		return Runtime.getRuntime().maxMemory() / 1024 / 1024;
	}

	static int cpuLoadPercent() {
		Object bean = ManagementFactory.getOperatingSystemMXBean();
		double load = invokeDouble(bean, "getCpuLoad", -1);
		if (load < 0)
			load = invokeDouble(bean, "getSystemCpuLoad", -1);
		if (load >= 0)
			return (int) Math.round(load * 100);
		return -1;
	}

	private static long invokeLong(Object target, String methodName, long fallback) {
		try {
			Method method = target.getClass().getMethod(methodName);
			Object value = method.invoke(target);
			return value instanceof Number number ? number.longValue() : fallback;
		} catch (Exception e) {
			return fallback;
		}
	}

	private static double invokeDouble(Object target, String methodName, double fallback) {
		try {
			Method method = target.getClass().getMethod(methodName);
			Object value = method.invoke(target);
			return value instanceof Number number ? number.doubleValue() : fallback;
		} catch (Exception e) {
			return fallback;
		}
	}
}
