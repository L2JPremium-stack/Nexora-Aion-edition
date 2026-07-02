package com.aionemu.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class ServerPanel extends UiUtils.RoundedPanel implements ServerProcess.Listener {

	private static final long serialVersionUID = 1L;
	private final ServerProcess process;
	private final JLabel statusValue = new JLabel("OFFLINE");
	private final JLabel uptimeValue = new JLabel("00:00:00");
	private final JLabel memoryValue = new JLabel("0 MB");
	private final JLabel portValue = new JLabel("-");
	private final JLabel mainClassValue = new JLabel("-");
	private final JLabel statusPill = new JLabel("OFFLINE");
	private final JProgressBar memoryBar = new JProgressBar(0, 100);
	private final JButton startButton;
	private final JButton restartButton;
	private final JButton stopButton;

	ServerPanel(ServerProcess process) {
		super(14, AppTheme.SURFACE, process.definition().accent());
		this.process = process;
		startButton = UiUtils.actionButton("Iniciar", AppTheme.GREEN_DARK, IconFactory.icon(IconFactory.Glyph.PLAY, Color.WHITE, 14));
		restartButton = UiUtils.actionButton("Reiniciar", AppTheme.ORANGE, IconFactory.icon(IconFactory.Glyph.RESTART, Color.WHITE, 14));
		stopButton = UiUtils.actionButton("Parar", AppTheme.RED, IconFactory.icon(IconFactory.Glyph.STOP, Color.WHITE, 14));
		process.addListener(this);
		setBorder(new EmptyBorder(14, 15, 14, 15));
		add(header(), BorderLayout.NORTH);
		add(details(), BorderLayout.CENTER);
		add(actions(), BorderLayout.SOUTH);
		new Timer(1000, _ -> refresh()).start();
		refresh();
	}

	private JPanel header() {
		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		header.setBorder(new EmptyBorder(0, 0, 10, 0));

		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		left.setOpaque(false);
		JLabel icon = new JLabel(cardIcon());
		icon.setPreferredSize(new Dimension(44, 44));
		JLabel title = new JLabel("<html><b>" + process.definition().title() + "</b><br><span style='font-size:10px;color:#9b8cff;'>" + process.definition().subtitle()
			+ "</span></html>");
		title.setFont(AppTheme.CARD_TITLE);
		left.add(icon);
		left.add(title);
		header.add(left, BorderLayout.WEST);

		statusPill.setOpaque(true);
		statusPill.setForeground(Color.WHITE);
		statusPill.setFont(AppTheme.FONT_BOLD);
		statusPill.setBorder(new EmptyBorder(6, 12, 6, 12));
		header.add(statusPill, BorderLayout.EAST);
		return header;
	}

	private JPanel details() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(row("Status:", statusValue));
		panel.add(row("Uptime:", uptimeValue));

		JPanel memoryRow = new JPanel(new BorderLayout(12, 0));
		memoryRow.setOpaque(false);
		JLabel label = new JLabel("Memoria:");
		label.setForeground(AppTheme.TEXT);
		label.setFont(AppTheme.FONT_MEDIUM);
		label.setPreferredSize(new Dimension(104, 26));
		memoryValue.setHorizontalAlignment(JLabel.RIGHT);
		memoryValue.setFont(AppTheme.FONT_BOLD);
		memoryValue.setForeground(AppTheme.TEXT);
		memoryBar.setOpaque(false);
		memoryBar.setStringPainted(true);
		memoryBar.setForeground(process.definition().accent());
		memoryBar.setBackground(AppTheme.SURFACE_3);
		memoryRow.add(label, BorderLayout.WEST);
		memoryRow.add(memoryBar, BorderLayout.CENTER);
		memoryRow.add(memoryValue, BorderLayout.EAST);
		memoryRow.setBorder(new EmptyBorder(6, 0, 6, 0));
		panel.add(memoryRow);

		panel.add(row("Porta:", portValue));
		panel.add(row("Classe Principal:", mainClassValue));
		return panel;
	}

	private static JPanel row(String labelText, JLabel value) {
		JPanel row = new JPanel(new BorderLayout(10, 0));
		row.setOpaque(false);
		row.setBorder(new EmptyBorder(5, 0, 5, 0));
		JLabel label = new JLabel(labelText);
		label.setForeground(AppTheme.TEXT);
		label.setFont(AppTheme.FONT_MEDIUM);
		label.setPreferredSize(new Dimension(104, 23));
		value.setHorizontalAlignment(JLabel.RIGHT);
		value.setForeground(AppTheme.TEXT);
		value.setFont(AppTheme.FONT_BOLD);
		row.add(label, BorderLayout.WEST);
		row.add(value, BorderLayout.CENTER);
		return row;
	}

	private JPanel actions() {
		JPanel wrapper = new JPanel();
		wrapper.setOpaque(false);
		wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

		JPanel first = new JPanel(new GridLayout(1, 3, 10, 0));
		first.setOpaque(false);
		first.add(startButton);
		first.add(restartButton);
		first.add(stopButton);

		JPanel second = new JPanel(new GridLayout(1, 2, 10, 0));
		second.setOpaque(false);
		JButton folder = UiUtils.ghostButton("Abrir Pasta", IconFactory.icon(IconFactory.Glyph.FOLDER, AppTheme.TEXT, 16));
		JButton console = UiUtils.ghostButton("Abrir Console", IconFactory.icon(IconFactory.Glyph.CONSOLE, AppTheme.TEXT, 16));
		folder.addActionListener(_ -> runIo(process::openFolder));
		console.addActionListener(_ -> runIo(process::openConsole));
		second.add(folder);
		second.add(console);

		startButton.addActionListener(_ -> runIo(process::start));
		restartButton.addActionListener(_ -> process.restart());
		stopButton.addActionListener(_ -> process.stop());

		wrapper.add(first);
		wrapper.add(Box.createVerticalStrut(12));
		wrapper.add(second);
		return wrapper;
	}

	private javax.swing.Icon cardIcon() {
		IconFactory.Glyph glyph = process.definition().kind() == ServerDefinition.Kind.LOGIN ? IconFactory.Glyph.LOGIN : IconFactory.Glyph.GAME;
		return IconFactory.icon(glyph, process.definition().accent(), 44);
	}

	private void refresh() {
		ServerDefinition definition = process.definition();
		boolean running = process.isRunning();
		statusValue.setText(running ? "ONLINE" : "OFFLINE");
		statusValue.setForeground(running ? AppTheme.GREEN : AppTheme.MUTED);
		statusPill.setText(running ? "ONLINE" : "OFFLINE");
		statusPill.setBackground(running ? AppTheme.GREEN_DARK : AppTheme.SURFACE_3);
		uptimeValue.setText(Utils.formatDuration(process.uptime()));
		portValue.setText(definition.port());
		mainClassValue.setText(definition.mainClass());

		int maxMb = Math.max(1, definition.maxMemoryMb());
		long usedMb = running ? process.memoryMb() : 0;
		int percent = (int) Math.min(100, Math.round(usedMb * 100.0 / maxMb));
		memoryBar.setValue(percent);
		memoryBar.setString(percent + "%");
		memoryValue.setText(Utils.formatMb(usedMb) + " / " + Utils.formatMb(maxMb));
		memoryBar.setForeground(definition.accent());

		startButton.setEnabled(!running);
		restartButton.setEnabled(running);
		stopButton.setEnabled(running);
	}

	private void runIo(IoAction action) {
		try {
			action.run();
		} catch (IOException e) {
			SwingUtilities.invokeLater(() -> javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Nexora Dashboard",
				javax.swing.JOptionPane.ERROR_MESSAGE));
		}
	}

	@Override
	public void onLog(ServerProcess process, String line) {
	}

	@Override
	public void onStateChanged(ServerProcess process) {
		SwingUtilities.invokeLater(this::refresh);
	}

	@FunctionalInterface
	private interface IoAction {
		void run() throws IOException;
	}
}
