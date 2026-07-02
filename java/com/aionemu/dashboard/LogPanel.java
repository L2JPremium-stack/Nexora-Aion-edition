package com.aionemu.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class LogPanel extends UiUtils.RoundedPanel {

	private static final long serialVersionUID = 1L;
	private static final int MAX_LINES = 2500;
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
	private enum Filter {
		ALL,
		LOGIN,
		GAME,
		DASHBOARD
	}

	private final List<Line> lines = new ArrayList<>();
	private final JTextPane textPane = new JTextPane();
	private final UiUtils.PromptField searchField = new UiUtils.PromptField("Buscar no log...");
	private final DefaultStyledDocument document = new DefaultStyledDocument();
	private final List<JButton> filterButtons = new ArrayList<>();
	private Filter activeFilter = Filter.ALL;

	LogPanel() {
		super(14, AppTheme.SURFACE, AppTheme.BORDER);
		setBorder(new EmptyBorder(14, 16, 14, 16));
		add(header(), BorderLayout.NORTH);
		textPane.setDocument(document);
		textPane.setEditable(false);
		textPane.setFont(AppTheme.MONO);
		textPane.setForeground(AppTheme.TEXT);
		textPane.setBackground(AppTheme.WINDOW);
		textPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		JScrollPane scrollPane = new JScrollPane(textPane);
		UiUtils.styleScrollPane(scrollPane);
		add(scrollPane, BorderLayout.CENTER);
		appendSystem("Dashboard ready.");
	}

	void append(ServerProcess process, String line) {
		ServerDefinition definition = process.definition();
		SwingUtilities.invokeLater(() -> {
			String decorated = LocalTime.now().format(TIME_FORMAT) + "  " + definition.title() + " - " + line;
			lines.add(new Line(decorated, colorFor(definition, line), sourceFor(definition)));
			boolean trimmed = false;
			if (lines.size() > MAX_LINES) {
				lines.remove(0);
				trimmed = true;
			}
			if (trimmed) {
				rebuild();
			} else {
				appendIfVisible(lines.get(lines.size() - 1));
				textPane.setCaretPosition(document.getLength());
			}
		});
	}

	void appendSystem(String line) {
		SwingUtilities.invokeLater(() -> {
			lines.add(new Line(LocalTime.now().format(TIME_FORMAT) + "  DASHBOARD - " + line, AppTheme.MUTED, Filter.DASHBOARD));
			if (lines.size() > MAX_LINES)
				lines.remove(0);
			rebuild();
		});
	}

	private JPanel header() {
		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		header.setBorder(new EmptyBorder(0, 0, 12, 0));
		JLabel title = new JLabel("LOGS", IconFactory.icon(IconFactory.Glyph.LOGS, AppTheme.TEXT, 20), JLabel.LEFT);
		title.setFont(AppTheme.CARD_TITLE);
		title.setIconTextGap(9);
		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		left.setOpaque(false);
		left.add(title);
		left.add(filterButton("Todos", Filter.ALL));
		left.add(filterButton("Login", Filter.LOGIN));
		left.add(filterButton("Game", Filter.GAME));
		left.add(filterButton("Painel", Filter.DASHBOARD));
		header.add(left, BorderLayout.WEST);

		JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		tools.setOpaque(false);
		searchField.setColumns(24);
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				rebuild();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				rebuild();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				rebuild();
			}
		});
		JButton clear = UiUtils.ghostButton("Limpar", IconFactory.icon(IconFactory.Glyph.TRASH, AppTheme.TEXT, 16));
		clear.addActionListener(this::clear);
		tools.add(searchField);
		tools.add(clear);
		header.add(tools, BorderLayout.EAST);
		return header;
	}

	private JButton filterButton(String text, Filter filter) {
		JButton button = UiUtils.chipButton(text, activeFilter == filter);
		button.addActionListener(_ -> {
			activeFilter = filter;
			refreshFilterButtons();
			rebuild();
		});
		filterButtons.add(button);
		return button;
	}

	private void refreshFilterButtons() {
		for (JButton button : filterButtons) {
			boolean active = switch (button.getText()) {
				case "Login" -> activeFilter == Filter.LOGIN;
				case "Game" -> activeFilter == Filter.GAME;
				case "Painel" -> activeFilter == Filter.DASHBOARD;
				default -> activeFilter == Filter.ALL;
			};
			button.setForeground(active ? Color.WHITE : AppTheme.MUTED);
			button.setBackground(active ? AppTheme.SIDEBAR_ACTIVE : AppTheme.SURFACE_3);
		}
	}

	private void clear(ActionEvent event) {
		lines.clear();
		rebuild();
	}

	private void rebuild() {
		SwingUtilities.invokeLater(() -> {
			try {
				document.remove(0, document.getLength());
				for (Line line : lines)
					appendIfVisible(line);
				textPane.setCaretPosition(document.getLength());
			} catch (BadLocationException e) {
				// The document is only modified on the Swing thread.
			}
		});
	}

	private void appendIfVisible(Line line) {
		if (activeFilter != Filter.ALL && line.source() != activeFilter)
			return;
		String filter = searchField.getText().trim().toLowerCase(Locale.ROOT);
		if (!filter.isEmpty() && !line.text().toLowerCase(Locale.ROOT).contains(filter))
			return;
		try {
			SimpleAttributeSet attributes = new SimpleAttributeSet();
			StyleConstants.setForeground(attributes, line.color());
			document.insertString(document.getLength(), line.text() + System.lineSeparator(), attributes);
		} catch (BadLocationException e) {
			// The document is only modified on the Swing thread.
		}
	}

	private static Color colorFor(ServerDefinition definition, String line) {
		String lower = line.toLowerCase(Locale.ROOT);
		if (lower.contains("error") || lower.contains("exception") || lower.contains("erro"))
			return new Color(255, 115, 115);
		if (lower.contains("warn"))
			return new Color(255, 187, 97);
		if (line.startsWith("[dashboard]"))
			return AppTheme.MUTED;
		return definition.kind() == ServerDefinition.Kind.LOGIN ? new Color(189, 141, 255) : new Color(139, 199, 255);
	}

	private static Filter sourceFor(ServerDefinition definition) {
		return definition.kind() == ServerDefinition.Kind.LOGIN ? Filter.LOGIN : Filter.GAME;
	}

	private record Line(String text, Color color, Filter source) {
	}
}
