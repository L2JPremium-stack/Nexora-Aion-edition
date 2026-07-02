package com.aionemu.dashboard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

final class DashboardFrame extends JFrame
{
	
	private static final long serialVersionUID = 1L;
	private final ConfigManager configManager;
	private final ServerProcess loginProcess;
	private final ServerProcess gameProcess;
	private final LogPanel logPanel = new LogPanel();
	private Point dragStart;
	
	DashboardFrame(ConfigManager configManager)
	{
		this.configManager = configManager;
		this.loginProcess = new ServerProcess(configManager.createLoginDefinition());
		this.gameProcess = new ServerProcess(configManager.createGameDefinition());
		loginProcess.addListener(logListener());
		gameProcess.addListener(logListener());
		
		setTitle("Nexora Aion Edition - Server Dashboard");
		setUndecorated(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(980, 620));
		setSize(1120, 700);
		setLocationRelativeTo(null);
		installWindowIcon();
		setContentPane(root());
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				closeDashboard();
			}
		});
	}
	
	private void installWindowIcon()
	{
		Path iconPath = configManager.rootDirectory().resolve("images/nexora_32x32.png");
		if (!Files.isRegularFile(iconPath))
			return;
		try
		{
			setIconImage(ImageIO.read(iconPath.toFile()));
		}
		catch (Exception e)
		{
			// The dashboard can run without the icon asset.
		}
	}
	
	private JPanel root()
	{
		UiUtils.RoundedPanel root = new UiUtils.RoundedPanel(18, AppTheme.WINDOW, AppTheme.BORDER);
		root.setLayout(new BorderLayout());
		root.add(header(), BorderLayout.NORTH);
		root.add(body(), BorderLayout.CENTER);
		return root;
	}
	
	private JPanel header()
	{
		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		header.setBorder(new EmptyBorder(14, 18, 12, 18));
		attachDrag(header);
		
		JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		logo.setOpaque(false);
		logo.add(new JLabel(IconFactory.diamond(AppTheme.PURPLE_BRIGHT, 36)));
		JLabel brand = new JLabel("<html><span style='font-size:22px;font-weight:bold;'>NEXORA</span><br><span style='font-size:10px;color:#b6bcc9;'>AION EDITION</span></html>");
		brand.setFont(AppTheme.FONT_BOLD);
		logo.add(brand);
		header.add(logo, BorderLayout.WEST);
		
		JLabel title = new JLabel("<html><div style='text-align:center;'><span style='font-size:25px;font-weight:bold;'>NEXORA CONTROL CENTER</span><br><span style='font-size:13px;color:#c7ccd7;'>Login, Game Server e MariaDB em um painel</span></div></html>", JLabel.CENTER);
		header.add(title, BorderLayout.CENTER);
		
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		controls.setOpaque(false);
		JButton settings = UiUtils.titleButton("Configuracoes");
		settings.setIcon(IconFactory.icon(IconFactory.Glyph.SETTINGS, AppTheme.MUTED, 18));
		settings.addActionListener(_ -> showSettings());
		JButton minimize = UiUtils.titleButton("-");
		minimize.addActionListener(_ -> setState(ICONIFIED));
		JButton maximize = UiUtils.titleButton("□");
		maximize.addActionListener(_ -> toggleMaximized());
		JButton close = UiUtils.titleButton("X");
		close.addActionListener(_ -> closeDashboard());
		controls.add(settings);
		controls.add(minimize);
		controls.add(maximize);
		controls.add(close);
		header.add(controls, BorderLayout.EAST);
		return header;
	}
	
	private JPanel body()
	{
		JPanel body = new JPanel(new BorderLayout());
		body.setOpaque(false);
		body.add(sidebar(), BorderLayout.WEST);
		body.add(content(), BorderLayout.CENTER);
		return body;
	}
	
	private JPanel sidebar()
	{
		UiUtils.RoundedPanel sidebar = new UiUtils.RoundedPanel(10, AppTheme.SIDEBAR, AppTheme.BORDER);
		sidebar.setPreferredSize(new Dimension(188, 0));
		sidebar.setBorder(new EmptyBorder(14, 9, 14, 9));
		JPanel stack = new JPanel();
		stack.setOpaque(false);
		stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
		stack.setAlignmentX(Component.LEFT_ALIGNMENT);
		stack.add(nav("Dashboard", IconFactory.Glyph.DASHBOARD, true));
		stack.add(Box.createVerticalStrut(6));
		stack.add(nav("Logs", IconFactory.Glyph.LOGS, false));
		stack.add(Box.createVerticalStrut(6));
		stack.add(nav("Banco MariaDB", IconFactory.Glyph.TOOLS, false));
		stack.add(Box.createVerticalStrut(6));
		stack.add(nav("Configuracoes", IconFactory.Glyph.SETTINGS, false));
		stack.add(Box.createVerticalStrut(6));
		stack.add(nav("Sobre", IconFactory.Glyph.INFO, false));
		stack.add(Box.createVerticalGlue());
		stack.add(versionBlock());
		sidebar.add(stack, BorderLayout.CENTER);
		return sidebar;
	}
	
	private Component nav(String text, IconFactory.Glyph glyph, boolean active)
	{
		JButton button = UiUtils.navButton(text, IconFactory.icon(glyph, AppTheme.TEXT, 18), active);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setMinimumSize(new Dimension(0, 42));
		button.setPreferredSize(new Dimension(170, 42));
		button.setMaximumSize(new Dimension(Short.MAX_VALUE, 42));
		if ("Configuracoes".equals(text))
			button.addActionListener(_ -> showSettings());
		if ("Logs".equals(text))
			button.addActionListener(_ -> logPanel.requestFocusInWindow());
		if ("Banco MariaDB".equals(text))
			button.addActionListener(_ -> openTools());
		if ("Sobre".equals(text))
			button.addActionListener(_ -> showAbout());
		return button;
	}
	
	private JPanel versionBlock()
	{
		UiUtils.RoundedPanel panel = new UiUtils.RoundedPanel(12, AppTheme.SURFACE, AppTheme.BORDER_SOFT);
		panel.setBorder(new EmptyBorder(12, 12, 12, 12));
		panel.setLayout(new GridLayout(3, 1, 0, 2));
		JLabel version = new JLabel("NEXORA " + configManager.version());
		JLabel edition = new JLabel("Aion Edition");
		JLabel hint = new JLabel("MariaDB ready");
		version.setFont(AppTheme.SMALL_BOLD);
		edition.setForeground(AppTheme.TEXT);
		hint.setForeground(AppTheme.MUTED);
		panel.add(version);
		panel.add(edition);
		panel.add(hint);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 246));
		return panel;
	}
	
	private JPanel content()
	{
		JPanel content = new JPanel(new BorderLayout(16, 16));
		content.setOpaque(false);
		content.setBorder(new EmptyBorder(16, 18, 18, 18));
		
		JPanel cards = new JPanel(new GridLayout(1, 2, 16, 0));
		cards.setOpaque(false);
		cards.add(new ServerPanel(loginProcess));
		cards.add(new ServerPanel(gameProcess));
		
		JPanel center = new JPanel(new BorderLayout(0, 16));
		center.setOpaque(false);
		center.add(cards, BorderLayout.NORTH);
		center.add(logPanel, BorderLayout.CENTER);
		content.add(center, BorderLayout.CENTER);
		content.add(new StatusBar(), BorderLayout.SOUTH);
		return content;
	}
	
	private ServerProcess.Listener logListener()
	{
		return new ServerProcess.Listener()
		{
			@Override
			public void onLog(ServerProcess process, String line)
			{
				logPanel.append(process, line);
			}
			
			@Override
			public void onStateChanged(ServerProcess process)
			{
			}
		};
	}
	
	private void attachDrag(Component component)
	{
		MouseAdapter adapter = new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				dragStart = e.getPoint();
			}
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				if (dragStart == null)
					return;
				if ((getExtendedState() & MAXIMIZED_BOTH) == MAXIMIZED_BOTH)
					return;
				Point location = getLocation();
				setLocation(location.x + e.getX() - dragStart.x, location.y + e.getY() - dragStart.y);
			}
		};
		component.addMouseListener(adapter);
		component.addMouseMotionListener(adapter);
	}
	
	private void showSettings()
	{
		SettingsDialog dialog = new SettingsDialog(this, configManager, loginProcess, gameProcess, () -> logPanel.appendSystem("Settings saved."));
		dialog.setVisible(true);
	}
	
	private void openTools()
	{
		DatabaseToolsDialog dialog = new DatabaseToolsDialog(this, configManager, message -> logPanel.appendSystem(message));
		dialog.setVisible(true);
	}

	private void toggleMaximized()
	{
		if ((getExtendedState() & MAXIMIZED_BOTH) == MAXIMIZED_BOTH)
			setExtendedState(NORMAL);
		else
			setExtendedState(MAXIMIZED_BOTH);
	}
	
	private void showAbout()
	{
		String message = "Nexora Aion Edition\nDashboard v" + configManager.version() + "\n" + configManager.rootDirectory();
		JOptionPane.showMessageDialog(this, message, "Sobre", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void closeDashboard()
	{
		if (loginProcess.isRunning() || gameProcess.isRunning())
		{
			int option = JOptionPane.showConfirmDialog(this, "Encerrar o dashboard e parar servidores ativos?", "Nexora Dashboard", JOptionPane.YES_NO_OPTION);
			if (option != JOptionPane.YES_OPTION)
				return;
		}
		Thread closeThread = new Thread(() -> {
			loginProcess.stopAndWait();
			gameProcess.stopAndWait();
			SwingUtilities.invokeLater(() -> {
				dispose();
				System.exit(0);
			});
		}, "nexora-close");
		closeThread.setDaemon(false);
		closeThread.start();
	}
}
