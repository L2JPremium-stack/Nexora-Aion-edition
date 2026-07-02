package com.aionemu.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

final class DatabaseToolsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final ConfigManager configManager;
	private final Consumer<String> dashboardLog;
	private final JTextPane output = new JTextPane();
	private final List<JButton> buttons = new ArrayList<>();

	DatabaseToolsDialog(DashboardFrame owner, ConfigManager configManager, Consumer<String> dashboardLog) {
		super(owner, "Banco MariaDB", false);
		this.configManager = configManager;
		this.dashboardLog = dashboardLog;
		setContentPane(content());
		setSize(820, 560);
		setLocationRelativeTo(owner);
	}

	private JPanel content() {
		UiUtils.RoundedPanel root = new UiUtils.RoundedPanel(14, AppTheme.SURFACE, AppTheme.BORDER);
		root.setBorder(new EmptyBorder(18, 20, 18, 20));

		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		JLabel title = new JLabel("<html><b>Banco MariaDB</b><br><span style='font-size:11px;color:#9ba4b5;'>Instala a estrutura usando os arquivos de configuracao do pacote</span></html>");
		title.setFont(AppTheme.CARD_TITLE);
		header.add(title, BorderLayout.WEST);
		header.add(actions(), BorderLayout.EAST);

		JPanel body = new JPanel(new BorderLayout(0, 14));
		body.setOpaque(false);
		body.add(profilePanel(), BorderLayout.NORTH);
		body.add(logOutput(), BorderLayout.CENTER);

		root.add(header, BorderLayout.NORTH);
		root.add(body, BorderLayout.CENTER);
		return root;
	}

	private JPanel actions() {
		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);
		actions.add(button("Testar", () -> runTask("Testando MariaDB", this::testConnections)));
		actions.add(button("Criar Bancos", () -> runTask("Criando bancos", this::createDatabases)));
		actions.add(button("Instalar SQL", () -> runTask("Instalando SQL", this::installSql)));
		actions.add(button("Registrar GS", () -> runTask("Registrando GameServer", this::registerGameServer)));
		actions.add(button("Abrir SQL", this::openSqlFolder));
		return actions;
	}

	private JButton button(String text, Runnable action) {
		JButton button = UiUtils.ghostButton(text, null);
		button.addActionListener(_ -> action.run());
		buttons.add(button);
		return button;
	}

	private JPanel profilePanel() {
		JPanel grid = new JPanel(new GridLayout(1, 3, 12, 0));
		grid.setOpaque(false);
		for (DatabaseProfile profile : profiles())
			grid.add(profileCard(profile));
		return grid;
	}

	private JPanel profileCard(DatabaseProfile profile) {
		UiUtils.RoundedPanel panel = new UiUtils.RoundedPanel(12, AppTheme.WINDOW, AppTheme.BORDER_SOFT);
		panel.setBorder(new EmptyBorder(12, 12, 12, 12));
		JPanel stack = new JPanel();
		stack.setOpaque(false);
		stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
		stack.add(label(profile.title(), AppTheme.TEXT, AppTheme.FONT_BOLD));
		stack.add(Box.createVerticalStrut(5));
		stack.add(label("Banco: " + profile.schema(), AppTheme.TEXT, AppTheme.FONT));
		stack.add(label("Usuario: " + profile.user(), AppTheme.MUTED, AppTheme.FONT));
		stack.add(label("Scripts base: " + profile.baseScripts().size(), AppTheme.MUTED, AppTheme.FONT));
		stack.add(label("Updates: " + profile.updateScripts().size(), AppTheme.MUTED, AppTheme.FONT));
		panel.add(stack, BorderLayout.CENTER);
		return panel;
	}

	private JScrollPane logOutput() {
		output.setEditable(false);
		output.setFont(AppTheme.MONO);
		output.setForeground(AppTheme.TEXT);
		output.setBackground(AppTheme.WINDOW);
		output.setBorder(new EmptyBorder(12, 12, 12, 12));
		append("Pronto. MariaDB recomendado: 10.6+ / 11.x. As credenciais serao lidas dos database.properties.");
		JScrollPane scrollPane = new JScrollPane(output);
		UiUtils.styleScrollPane(scrollPane);
		return scrollPane;
	}

	private void runTask(String title, SqlTask task) {
		setButtonsEnabled(false);
		append("");
		append("== " + title + " ==");
		Thread worker = new Thread(() -> {
			try {
				task.run();
				append("Concluido.");
				dashboardLog.accept(title + " concluido.");
			} catch (Exception e) {
				append("ERRO: " + e.getMessage());
				dashboardLog.accept(title + " falhou: " + e.getMessage());
			} finally {
				SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
			}
		}, "nexora-db-tools");
		worker.setDaemon(true);
		worker.start();
	}

	private void testConnections() throws Exception {
		for (DatabaseProfile profile : profiles()) {
			try (Connection ignored = DriverManager.getConnection(profile.rootUrl(), profile.user(), profile.password())) {
				append(profile.title() + ": conexao OK em " + profile.rootUrl());
			}
		}
	}

	private void createDatabases() throws Exception {
		for (DatabaseProfile profile : profiles()) {
			try (Connection connection = DriverManager.getConnection(profile.rootUrl(), profile.user(), profile.password());
				Statement statement = connection.createStatement()) {
				statement.execute("CREATE DATABASE IF NOT EXISTS `" + profile.schema() + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
				append(profile.title() + ": banco `" + profile.schema() + "` pronto.");
			}
		}
	}

	private void installSql() throws Exception {
		createDatabases();
		for (DatabaseProfile profile : profiles()) {
			try (Connection connection = DriverManager.getConnection(profile.databaseUrl(), profile.user(), profile.password())) {
				for (Path script : profile.baseScripts())
					executeSqlFile(connection, script, false);
				for (Path script : profile.updateScripts())
					executeSqlFile(connection, script, true);
				if (profile.baseScripts().isEmpty())
					append(profile.title() + ": nenhum SQL base encontrado no pacote.");
			}
		}
	}

	private void registerGameServer() throws Exception {
		DatabaseProfile login = profiles().stream().filter(profile -> profile.title().equals("Login Server")).findFirst()
			.orElseThrow(() -> new IllegalStateException("Config do Login Server nao encontrada."));
		Properties network = loadProperties(configManager.rootDirectory().resolve("game-server/config/network/network.properties"));
		int gsId = Integer.parseInt(network.getProperty("gameserver.network.login.gsid", "1").trim());
		String password = network.getProperty("gameserver.network.login.password", "1234").trim();
		try (Connection connection = DriverManager.getConnection(login.databaseUrl(), login.user(), login.password());
			PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO `gameservers` (`id`, `mask`, `password`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `mask`=VALUES(`mask`), `password`=VALUES(`password`)")) {
			statement.setInt(1, gsId);
			statement.setString(2, "127.0.0.1");
			statement.setString(3, password);
			statement.executeUpdate();
			append("GameServer registrado: id=" + gsId + ", mask=127.0.0.1.");
		}
	}

	private void executeSqlFile(Connection connection, Path script, boolean continueOnError) throws Exception {
		if (!Files.isRegularFile(script)) {
			append("Ignorando ausente: " + script);
			return;
		}
		append("Executando " + configManager.rootDirectory().relativize(script) + "...");
		int executed = 0;
		try (Statement statement = connection.createStatement()) {
			for (String sql : splitSql(Files.readString(script, StandardCharsets.UTF_8))) {
				try {
					statement.execute(sql);
					executed++;
				} catch (SQLException e) {
					append("Aviso em " + script.getFileName() + ": " + e.getMessage());
					if (!continueOnError)
						throw e;
				}
			}
		}
		append(script.getFileName() + ": " + executed + " comandos.");
	}

	private List<String> splitSql(String content) {
		List<String> statements = new ArrayList<>();
		String delimiter = ";";
		StringBuilder current = new StringBuilder();
		for (String line : content.split("\\R")) {
			String trimmed = line.trim();
			if (trimmed.toUpperCase(Locale.ROOT).startsWith("DELIMITER ")) {
				delimiter = trimmed.substring("DELIMITER ".length()).trim();
				continue;
			}
			current.append(line).append('\n');
			String sql = current.toString().trim();
			if (sql.endsWith(delimiter)) {
				sql = sql.substring(0, sql.length() - delimiter.length()).trim();
				if (!sql.isBlank())
					statements.add(sql);
				current.setLength(0);
			}
		}
		String tail = current.toString().trim();
		if (!tail.isBlank())
			statements.add(tail);
		return statements;
	}

	private List<DatabaseProfile> profiles() {
		Path root = configManager.rootDirectory();
		return List.of(
			profile("Login Server", root.resolve("login-server/config/network/database.properties"), List.of(root.resolve("tools/sql/login/aion_ls.sql")),
				List.of(root.resolve("tools/sql/login/update.sql"))),
			profile("Game Server", root.resolve("game-server/config/network/database.properties"), List.of(root.resolve("tools/sql/game/aion_gs.sql")),
				List.of(root.resolve("tools/sql/game/update_mariadb_safe.sql"))),
			profile("Chat Server", root.resolve("chat-server/config/network/database.properties"), List.of(root.resolve("tools/sql/chat/aion_cs.sql")), List.of()));
	}

	private DatabaseProfile profile(String title, Path configFile, List<Path> baseScripts, List<Path> updateScripts) {
		Properties properties = loadProperties(configFile);
		String url = normalizeUrl(properties.getProperty("database.url", "jdbc:mysql://localhost:3306/" + title.toLowerCase(Locale.ROOT).replace(" ", "_")));
		String user = properties.getProperty("database.user", "root").trim();
		String password = properties.getProperty("database.password", "").trim();
		String schema = schemaName(url);
		return new DatabaseProfile(title, configFile, url, rootUrl(url), schema, user, password, existing(baseScripts), existing(updateScripts));
	}

	private static Properties loadProperties(Path file) {
		Properties properties = new Properties();
		if (!Files.isRegularFile(file))
			return properties;
		try (var in = Files.newInputStream(file)) {
			properties.load(in);
		} catch (IOException e) {
			throw new IllegalStateException("Nao foi possivel ler " + file + ": " + e.getMessage(), e);
		}
		return properties;
	}

	private static List<Path> existing(List<Path> scripts) {
		return scripts.stream().filter(Files::isRegularFile).toList();
	}

	private static String normalizeUrl(String url) {
		String normalized = url.replace("${gameserver.timezone}", "UTC").replace("serverTimezone=&", "serverTimezone=UTC&");
		if (!normalized.contains("useSSL="))
			normalized += normalized.contains("?") ? "&useSSL=false" : "?useSSL=false";
		if (!normalized.contains("allowPublicKeyRetrieval="))
			normalized += "&allowPublicKeyRetrieval=true";
		return normalized;
	}

	private static String schemaName(String url) {
		int query = url.indexOf('?');
		String noQuery = query >= 0 ? url.substring(0, query) : url;
		int slash = noQuery.lastIndexOf('/');
		return slash >= 0 ? noQuery.substring(slash + 1) : noQuery;
	}

	private static String rootUrl(String url) {
		int schemeEnd = url.indexOf("://");
		int slash = url.indexOf('/', schemeEnd + 3);
		int query = url.indexOf('?');
		String host = slash >= 0 ? url.substring(0, slash + 1) : url + "/";
		String params = query >= 0 ? url.substring(query) : "";
		return host + params;
	}

	private void openSqlFolder() {
		try {
			Utils.openFolder(configManager.rootDirectory().resolve("tools/sql"));
		} catch (IOException e) {
			append("ERRO: " + e.getMessage());
		}
	}

	private void setButtonsEnabled(boolean enabled) {
		for (JButton button : buttons)
			button.setEnabled(enabled);
	}

	private JLabel label(String text, java.awt.Color color, java.awt.Font font) {
		JLabel label = new JLabel(text);
		label.setForeground(color);
		label.setFont(font);
		return label;
	}

	private void append(String text) {
		SwingUtilities.invokeLater(() -> {
			try {
				output.getDocument().insertString(output.getDocument().getLength(), text + System.lineSeparator(), null);
				output.setCaretPosition(output.getDocument().getLength());
			} catch (Exception e) {
				// The output document is only updated on the Swing thread.
			}
		});
	}

	@FunctionalInterface
	private interface SqlTask {
		void run() throws Exception;
	}

	private record DatabaseProfile(String title, Path configFile, String databaseUrl, String rootUrl, String schema, String user, String password,
		List<Path> baseScripts, List<Path> updateScripts) {
	}
}
