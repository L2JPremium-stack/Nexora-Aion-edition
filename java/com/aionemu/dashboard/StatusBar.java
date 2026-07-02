package com.aionemu.dashboard;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class StatusBar extends UiUtils.RoundedPanel {

	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final JLabel javaValue = new JLabel();
	private final JLabel ramValue = new JLabel();
	private final JLabel cpuValue = new JLabel();
	private final JLabel clockValue = new JLabel();

	StatusBar() {
		super(12, AppTheme.SURFACE, AppTheme.BORDER);
		setBorder(new EmptyBorder(12, 18, 12, 18));
		JPanel grid = new JPanel(new GridLayout(1, 4, 24, 0));
		grid.setOpaque(false);
		grid.add(tile(IconFactory.Glyph.JAVA, "Java:", javaValue));
		grid.add(tile(IconFactory.Glyph.RAM, "RAM Total:", ramValue));
		grid.add(tile(IconFactory.Glyph.CPU, "CPU:", cpuValue));
		grid.add(tile(IconFactory.Glyph.CLOCK, "", clockValue));
		add(grid, BorderLayout.CENTER);
		new Timer(1000, _ -> refresh()).start();
		refresh();
	}

	private static JPanel tile(IconFactory.Glyph glyph, String label, JLabel value) {
		JPanel tile = new JPanel(new BorderLayout(10, 0));
		tile.setOpaque(false);
		JLabel icon = new JLabel(IconFactory.icon(glyph, AppTheme.TEXT, 28));
		JLabel text = new JLabel();
		text.setFont(AppTheme.FONT);
		text.setForeground(AppTheme.TEXT);
		value.setForeground(AppTheme.TEXT);
		value.setFont(AppTheme.FONT_BOLD);
		if (label.isBlank())
			text.setText("");
		else
			text.setText(label);
		JPanel stack = new JPanel(new GridLayout(2, 1, 0, 0));
		stack.setOpaque(false);
		stack.add(text);
		stack.add(value);
		tile.add(icon, BorderLayout.WEST);
		tile.add(stack, BorderLayout.CENTER);
		return tile;
	}

	private void refresh() {
		javaValue.setText(System.getProperty("java.version") + " (" + System.getProperty("sun.arch.data.model", "64") + "-bit)");
		ramValue.setText(Utils.formatMb(Utils.totalMemoryMb()));
		int cpu = Utils.cpuLoadPercent();
		cpuValue.setText(cpu >= 0 ? cpu + "%" : "N/D");
		clockValue.setText(LocalTime.now().format(TIME) + "   " + LocalDate.now().format(DATE));
	}
}
