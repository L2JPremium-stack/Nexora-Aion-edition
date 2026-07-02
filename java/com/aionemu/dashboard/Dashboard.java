package com.aionemu.dashboard;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;

public final class Dashboard {

	private Dashboard() {
	}

	public static void main(String[] args) {
		System.setProperty("sun.java2d.uiScale.enabled", "true");
		EventQueue.invokeLater(() -> {
			AppTheme.install();
			DashboardFrame frame = new DashboardFrame(new ConfigManager());
			frame.setVisible(true);
			SwingUtilities.updateComponentTreeUI(frame);
		});
	}
}
