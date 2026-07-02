package com.aionemu.dashboard;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class UiUtils
{
	
	private UiUtils()
	{
	}
	
	static JButton actionButton(String text, Color color, Icon icon)
	{
		JButton button = new PaintedButton(text, icon, 9);
		button.setFocusPainted(false);
		button.setBorder(new EmptyBorder(10, 16, 10, 16));
		button.setForeground(Color.WHITE);
		button.setBackground(color);
		button.setFont(AppTheme.FONT_BOLD);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.addMouseListener(new HoverFill(button, color, color.brighter()));
		return button;
	}
	
	static JButton ghostButton(String text, Icon icon)
	{
		JButton button = new PaintedButton(text, icon, 9);
		button.setFocusPainted(false);
		button.setBorder(new EmptyBorder(10, 14, 10, 14));
		button.setForeground(AppTheme.TEXT);
		button.setBackground(AppTheme.SURFACE_3);
		button.setFont(AppTheme.FONT_MEDIUM);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.addMouseListener(new HoverFill(button, AppTheme.SURFACE_3, new Color(38, 47, 65)));
		return button;
	}
	
	static JButton navButton(String text, Icon icon, boolean active)
	{
		JButton button = new PaintedButton(text, icon, 9);
		button.setHorizontalAlignment(JButton.LEFT);
		button.setIconTextGap(10);
		button.setFocusPainted(false);
		button.setBorder(new EmptyBorder(10, 12, 10, 12));
		button.setForeground(AppTheme.TEXT);
		button.setFont(AppTheme.FONT_MEDIUM);
		button.setBackground(active ? AppTheme.SIDEBAR_ACTIVE : AppTheme.SIDEBAR);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.addMouseListener(new HoverFill(button, button.getBackground(), active ? AppTheme.PURPLE : AppTheme.SURFACE_2));
		return button;
	}

	static JButton chipButton(String text, boolean active)
	{
		JButton button = new PaintedButton(text, null, 8);
		button.setFocusPainted(false);
		button.setBorder(new EmptyBorder(8, 13, 8, 13));
		button.setForeground(active ? Color.WHITE : AppTheme.MUTED);
		button.setFont(AppTheme.SMALL_BOLD);
		button.setBackground(active ? AppTheme.SIDEBAR_ACTIVE : AppTheme.SURFACE_3);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.addMouseListener(new HoverFill(button, button.getBackground(), active ? AppTheme.PURPLE : new Color(42, 51, 69)));
		return button;
	}
	
	static JButton titleButton(String text)
	{
		JButton button = new JButton(text);
		button.setFont(AppTheme.FONT_BOLD);
		button.setForeground(AppTheme.MUTED);
		button.setBorder(new EmptyBorder(8, 12, 8, 12));
		button.setFocusPainted(false);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return button;
	}
	
	static JScrollPane scrollPane(JPanel panel)
	{
		JScrollPane scrollPane = new JScrollPane(panel);
		styleScrollPane(scrollPane);
		return scrollPane;
	}
	
	static void styleScrollPane(JScrollPane scrollPane)
	{
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		JScrollBar bar = scrollPane.getVerticalScrollBar();
		bar.setUnitIncrement(16);
		bar.setBackground(AppTheme.SURFACE);
		bar.setForeground(AppTheme.BORDER);
	}
	
	public static class RoundedPanel extends JPanel
	{
		
		private static final long serialVersionUID = 1L;
		private final int radius;
		private final Color background;
		private final Color border;
		
		RoundedPanel(int radius, Color background, Color border)
		{
			super(new BorderLayout());
			this.radius = radius;
			this.background = background;
			this.border = border;
			setOpaque(false);
		}
		
		@Override
		protected void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(background);
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
			if (border != null)
			{
				g2.setColor(border);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
			}
			g2.dispose();
			super.paintComponent(g);
		}
	}
	
	public static final class PromptField extends JTextField
	{
		
		private static final long serialVersionUID = 1L;
		private final String prompt;
		
		PromptField(String prompt)
		{
			this.prompt = prompt;
			setOpaque(false);
			setForeground(AppTheme.TEXT);
			setCaretColor(AppTheme.TEXT);
			setBorder(new EmptyBorder(9, 12, 9, 12));
			setFont(AppTheme.FONT);
		}
		
		@Override
		protected void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(AppTheme.WINDOW);
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
			g2.setColor(AppTheme.BORDER);
			g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
			g2.dispose();
			super.paintComponent(g);
			if (getText().isEmpty() && !isFocusOwner())
			{
				Graphics2D promptGraphics = (Graphics2D) g.create();
				promptGraphics.setColor(AppTheme.MUTED);
				Insets insets = getInsets();
				promptGraphics.setFont(getFont());
				promptGraphics.drawString(prompt, insets.left, getHeight() / 2 + promptGraphics.getFontMetrics().getAscent() / 2 - 2);
				promptGraphics.dispose();
			}
		}
	}
	
	private static final class HoverFill extends MouseAdapter
	{
		
		private final JButton button;
		private final Color normal;
		private final Color hover;
		
		private HoverFill(JButton button, Color normal, Color hover)
		{
			this.button = button;
			this.normal = normal;
			this.hover = hover;
		}
		
		@Override
		public void mouseEntered(MouseEvent e)
		{
			if (button.isEnabled())
				button.setBackground(hover);
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			button.setBackground(normal);
		}
	}
	
	private static final class PaintedButton extends JButton
	{
		
		private static final long serialVersionUID = 1L;
		private final int radius;
		
		private PaintedButton(String text, Icon icon, int radius)
		{
			super(text, icon);
			this.radius = radius;
		}
		
		@Override
		protected void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (!isEnabled())
				g2.setComposite(AlphaComposite.SrcOver.derive(0.45f));
			g2.setColor(getBackground());
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
			g2.dispose();
			super.paintComponent(g);
		}
	}
}
