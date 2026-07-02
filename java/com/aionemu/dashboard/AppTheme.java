package com.aionemu.dashboard;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;

final class AppTheme {

	static final Color WINDOW = new Color(8, 12, 20);
	static final Color SURFACE = new Color(15, 20, 31);
	static final Color SURFACE_2 = new Color(21, 27, 40);
	static final Color SURFACE_3 = new Color(28, 35, 50);
	static final Color SIDEBAR = new Color(11, 16, 25);
	static final Color SIDEBAR_ACTIVE = new Color(82, 55, 138);
	static final Color BORDER = new Color(49, 59, 78);
	static final Color BORDER_SOFT = new Color(36, 45, 62);
	static final Color TEXT = new Color(232, 237, 247);
	static final Color MUTED = new Color(155, 164, 181);
	static final Color GREEN = new Color(33, 186, 82);
	static final Color GREEN_DARK = new Color(20, 91, 52);
	static final Color RED = new Color(214, 55, 51);
	static final Color ORANGE = new Color(207, 116, 21);
	static final Color PURPLE = new Color(132, 75, 213);
	static final Color PURPLE_BRIGHT = new Color(170, 89, 235);
	static final Color BLUE = new Color(67, 145, 220);

	static final Font FONT = new Font("Segoe UI", Font.PLAIN, 13);
	static final Font FONT_MEDIUM = new Font("Segoe UI", Font.PLAIN, 14);
	static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
	static final Font TITLE = new Font("Segoe UI", Font.BOLD, 24);
	static final Font CARD_TITLE = new Font("Segoe UI", Font.BOLD, 21);
	static final Font SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 12);
	static final Font MONO = new Font("Consolas", Font.PLAIN, 13);

	private AppTheme() {
	}

	static void install() {
		UIManager.put("Panel.background", WINDOW);
		UIManager.put("Label.foreground", TEXT);
		UIManager.put("Button.font", FONT_MEDIUM);
		UIManager.put("Label.font", FONT);
		UIManager.put("TextField.font", FONT);
		UIManager.put("TextField.caretForeground", TEXT);
		UIManager.put("OptionPane.background", SURFACE);
		UIManager.put("OptionPane.messageForeground", TEXT);
		UIManager.put("OptionPane.messageFont", FONT);
		UIManager.put("OptionPane.buttonFont", FONT_MEDIUM);
	}

	static Border border(Color color) {
		return BorderFactory.createLineBorder(color, 1);
	}
}
