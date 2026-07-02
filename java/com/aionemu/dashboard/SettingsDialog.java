package com.aionemu.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

final class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final ConfigManager configManager;
	private final ServerProcess loginProcess;
	private final ServerProcess gameProcess;
	private final Runnable onSaved;
	private final JTextField javaField = field();
	private final JTextField loginXmsField = field();
	private final JTextField loginXmxField = field();
	private final JTextField gameXmsField = field();
	private final JTextField gameXmxField = field();

	SettingsDialog(DashboardFrame owner, ConfigManager configManager, ServerProcess loginProcess, ServerProcess gameProcess, Runnable onSaved) {
		super(owner, "Configuracoes", true);
		this.configManager = configManager;
		this.loginProcess = loginProcess;
		this.gameProcess = gameProcess;
		this.onSaved = onSaved;
		setContentPane(content());
		setSize(640, 390);
		setLocationRelativeTo(owner);
	}

	private JPanel content() {
		UiUtils.RoundedPanel root = new UiUtils.RoundedPanel(14, AppTheme.SURFACE, AppTheme.BORDER);
		root.setBorder(new EmptyBorder(20, 22, 20, 22));
		JLabel title = new JLabel("<html><b>Configuracoes</b><br><span style='font-size:11px;color:#9ba4b5;'>Nexora Control Center v" + configManager.version() + "</span></html>");
		title.setFont(AppTheme.CARD_TITLE);
		title.setBorder(new EmptyBorder(0, 0, 16, 0));
		root.add(title, BorderLayout.NORTH);
		root.add(form(), BorderLayout.CENTER);
		root.add(actions(), BorderLayout.SOUTH);
		return root;
	}

	private JPanel form() {
		JPanel form = new JPanel(new GridLayout(1, 2, 14, 0));
		form.setOpaque(false);
		form.add(card("Runtime", rows(new Row("Java", javaField, configManager.javaExecutable()))));
		form.add(card("Memoria", rows(new Row("Login Xms", loginXmsField, configManager.loginXms()), new Row("Login Xmx", loginXmxField, configManager.loginXmx()),
			new Row("Game Xms", gameXmsField, configManager.gameXms()), new Row("Game Xmx", gameXmxField, configManager.gameXmx()))));
		return form;
	}

	private JPanel actions() {
		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		actions.setOpaque(false);
		JButton cancel = UiUtils.ghostButton("Cancelar", null);
		JButton save = UiUtils.actionButton("Salvar", AppTheme.PURPLE, null);
		cancel.addActionListener(_ -> dispose());
		save.addActionListener(_ -> save());
		actions.add(cancel);
		actions.add(save);
		return actions;
	}

	private JPanel card(String titleText, JPanel content) {
		UiUtils.RoundedPanel card = new UiUtils.RoundedPanel(12, AppTheme.WINDOW, AppTheme.BORDER_SOFT);
		card.setBorder(new EmptyBorder(14, 14, 14, 14));
		JLabel title = new JLabel(titleText);
		title.setFont(AppTheme.FONT_BOLD);
		title.setBorder(new EmptyBorder(0, 0, 10, 0));
		card.add(title, BorderLayout.NORTH);
		card.add(content, BorderLayout.CENTER);
		return card;
	}

	private JPanel rows(Row... rows) {
		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(7, 0, 7, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = 0;
		for (Row row : rows)
			addRow(form, gbc, row.label(), row.field(), row.value());
		return form;
	}

	private static void addRow(JPanel form, GridBagConstraints gbc, String labelText, JTextField field, String value) {
		JLabel label = new JLabel(labelText);
		label.setForeground(AppTheme.TEXT);
		label.setFont(AppTheme.FONT_MEDIUM);
		field.setText(value);
		gbc.gridx = 0;
		gbc.weightx = 0.0;
		form.add(label, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		form.add(field, gbc);
		gbc.gridy++;
	}

	private void save() {
		try {
			configManager.saveRuntimeSettings(javaField.getText(), loginXmsField.getText(), loginXmxField.getText(), gameXmsField.getText(), gameXmxField.getText());
			loginProcess.updateDefinition(configManager.createLoginDefinition());
			gameProcess.updateDefinition(configManager.createGameDefinition());
			onSaved.run();
			dispose();
		} catch (IOException e) {
			SwingUtilities.invokeLater(() -> javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Nexora Dashboard",
				javax.swing.JOptionPane.ERROR_MESSAGE));
		}
	}

	private static JTextField field() {
		JTextField field = new JTextField();
		field.setFont(AppTheme.FONT);
		field.setForeground(AppTheme.TEXT);
		field.setCaretColor(AppTheme.TEXT);
		field.setBackground(AppTheme.WINDOW);
		field.setBorder(new EmptyBorder(9, 10, 9, 10));
		return field;
	}

	private record Row(String label, JTextField field, String value) {
	}
}
