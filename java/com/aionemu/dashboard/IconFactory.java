package com.aionemu.dashboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;

final class IconFactory {

	enum Glyph {
		DASHBOARD,
		LOGIN,
		GAME,
		LOGS,
		SETTINGS,
		TOOLS,
		INFO,
		PLAY,
		STOP,
		RESTART,
		FOLDER,
		CONSOLE,
		JAVA,
		RAM,
		CPU,
		CLOCK,
		SEARCH,
		TRASH
	}

	private IconFactory() {
	}

	static Icon icon(Glyph glyph, Color color, int size) {
		return new DrawnIcon(glyph, color, size);
	}

	private static final class DrawnIcon implements Icon {

		private final Glyph glyph;
		private final Color color;
		private final int size;

		private DrawnIcon(Glyph glyph, Color color, int size) {
			this.glyph = glyph;
			this.color = color;
			this.size = size;
		}

		@Override
		public int getIconWidth() {
			return size;
		}

		@Override
		public int getIconHeight() {
			return size;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.translate(x, y);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(color);
			g2.setStroke(new BasicStroke(Math.max(1.5f, size / 12f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			switch (glyph) {
				case DASHBOARD -> dashboard(g2);
				case LOGIN -> shield(g2);
				case GAME -> swords(g2);
				case LOGS -> logs(g2);
				case SETTINGS -> gear(g2);
				case TOOLS -> tools(g2);
				case INFO -> info(g2);
				case PLAY -> play(g2);
				case STOP -> stop(g2);
				case RESTART -> restart(g2);
				case FOLDER -> folder(g2);
				case CONSOLE -> console(g2);
				case JAVA -> java(g2);
				case RAM -> ram(g2);
				case CPU -> cpu(g2);
				case CLOCK -> clock(g2);
				case SEARCH -> search(g2);
				case TRASH -> trash(g2);
			}
			g2.dispose();
		}

		private void dashboard(Graphics2D g2) {
			int gap = size / 8;
			int cell = (size - gap * 3) / 2;
			for (int row = 0; row < 2; row++) {
				for (int col = 0; col < 2; col++) {
					g2.fillRoundRect(gap + col * (cell + gap), gap + row * (cell + gap), cell, cell, 3, 3);
				}
			}
		}

		private void shield(Graphics2D g2) {
			Path2D path = new Path2D.Double();
			path.moveTo(size * 0.5, size * 0.08);
			path.lineTo(size * 0.82, size * 0.22);
			path.lineTo(size * 0.74, size * 0.70);
			path.lineTo(size * 0.5, size * 0.92);
			path.lineTo(size * 0.26, size * 0.70);
			path.lineTo(size * 0.18, size * 0.22);
			path.closePath();
			g2.draw(path);
			g2.drawLine(size / 2, size / 4, size / 2, (int) (size * 0.68));
			g2.drawLine(size / 2, (int) (size * 0.68), (int) (size * 0.64), (int) (size * 0.48));
		}

		private void swords(Graphics2D g2) {
			g2.drawLine(size / 5, size / 5, size * 4 / 5, size * 4 / 5);
			g2.drawLine(size * 4 / 5, size / 5, size / 5, size * 4 / 5);
			g2.drawLine(size / 4, size * 3 / 4, size / 8, size * 7 / 8);
			g2.drawLine(size * 3 / 4, size * 3 / 4, size * 7 / 8, size * 7 / 8);
		}

		private void logs(Graphics2D g2) {
			g2.drawRoundRect(size / 5, size / 8, size * 3 / 5, size * 3 / 4, 3, 3);
			g2.drawLine(size / 3, size / 3, size * 2 / 3, size / 3);
			g2.drawLine(size / 3, size / 2, size * 2 / 3, size / 2);
			g2.drawLine(size / 3, size * 2 / 3, size / 2, size * 2 / 3);
		}

		private void gear(Graphics2D g2) {
			g2.draw(new Ellipse2D.Double(size * 0.30, size * 0.30, size * 0.40, size * 0.40));
			g2.draw(new Ellipse2D.Double(size * 0.43, size * 0.43, size * 0.14, size * 0.14));
			for (int i = 0; i < 8; i++) {
				double angle = i * Math.PI / 4;
				int x1 = (int) (size * 0.5 + Math.cos(angle) * size * 0.30);
				int y1 = (int) (size * 0.5 + Math.sin(angle) * size * 0.30);
				int x2 = (int) (size * 0.5 + Math.cos(angle) * size * 0.43);
				int y2 = (int) (size * 0.5 + Math.sin(angle) * size * 0.43);
				g2.drawLine(x1, y1, x2, y2);
			}
		}

		private void tools(Graphics2D g2) {
			g2.drawLine(size / 5, size / 6, size * 5 / 6, size * 5 / 6);
			g2.drawLine(size * 3 / 5, size / 5, size * 4 / 5, size / 5);
			g2.drawLine(size * 4 / 5, size / 5, size * 4 / 5, size * 2 / 5);
			g2.drawLine(size / 4, size * 4 / 5, size / 6, size * 5 / 6);
		}

		private void info(Graphics2D g2) {
			g2.drawOval(size / 8, size / 8, size * 3 / 4, size * 3 / 4);
			g2.fillOval(size / 2 - 1, size / 4, 3, 3);
			g2.drawLine(size / 2, size * 2 / 5, size / 2, size * 3 / 4);
		}

		private void play(Graphics2D g2) {
			Path2D path = new Path2D.Double();
			path.moveTo(size * 0.32, size * 0.22);
			path.lineTo(size * 0.76, size * 0.50);
			path.lineTo(size * 0.32, size * 0.78);
			path.closePath();
			g2.fill(path);
		}

		private void stop(Graphics2D g2) {
			g2.fillRoundRect(size / 3, size / 3, size / 3, size / 3, 2, 2);
		}

		private void restart(Graphics2D g2) {
			g2.drawArc(size / 5, size / 5, size * 3 / 5, size * 3 / 5, 40, 280);
			Path2D path = new Path2D.Double();
			path.moveTo(size * 0.75, size * 0.18);
			path.lineTo(size * 0.88, size * 0.22);
			path.lineTo(size * 0.78, size * 0.34);
			path.closePath();
			g2.fill(path);
		}

		private void folder(Graphics2D g2) {
			g2.drawRoundRect(size / 7, size / 3, size * 5 / 7, size * 4 / 9, 3, 3);
			g2.drawLine(size / 6, size / 3, size / 3, size / 3);
			g2.drawLine(size / 3, size / 3, size * 2 / 5, size / 4);
			g2.drawLine(size * 2 / 5, size / 4, size * 4 / 7, size / 4);
		}

		private void console(Graphics2D g2) {
			g2.drawRoundRect(size / 8, size / 5, size * 3 / 4, size * 3 / 5, 3, 3);
			g2.drawLine(size / 3, size * 4 / 5, size * 2 / 3, size * 4 / 5);
			g2.drawLine(size / 2, size * 4 / 5, size / 2, size * 9 / 10);
		}

		private void java(Graphics2D g2) {
			g2.drawArc(size / 4, size / 2, size / 2, size / 4, 180, 180);
			g2.drawLine(size / 4, size / 2 + size / 8, size / 4, size / 3);
			g2.drawLine(size * 3 / 4, size / 2 + size / 8, size * 3 / 4, size / 3);
			g2.drawArc(size / 3, size / 10, size / 3, size / 4, 300, 160);
		}

		private void ram(Graphics2D g2) {
			g2.drawRoundRect(size / 6, size / 4, size * 2 / 3, size / 2, 3, 3);
			for (int i = 0; i < 4; i++) {
				int x = size / 4 + i * size / 8;
				g2.drawLine(x, size / 6, x, size / 4);
				g2.drawLine(x, size * 3 / 4, x, size * 5 / 6);
			}
		}

		private void cpu(Graphics2D g2) {
			g2.drawLine(size / 5, size * 4 / 5, size * 4 / 5, size * 4 / 5);
			g2.drawLine(size / 5, size * 4 / 5, size / 5, size / 5);
			g2.drawLine(size / 4, size * 3 / 4, size / 2, size / 2);
			g2.drawLine(size / 2, size / 2, size * 2 / 3, size * 3 / 5);
			g2.drawLine(size * 2 / 3, size * 3 / 5, size * 4 / 5, size / 3);
		}

		private void clock(Graphics2D g2) {
			g2.drawOval(size / 8, size / 8, size * 3 / 4, size * 3 / 4);
			g2.drawLine(size / 2, size / 2, size / 2, size / 4);
			g2.drawLine(size / 2, size / 2, size * 2 / 3, size * 3 / 5);
		}

		private void search(Graphics2D g2) {
			g2.drawOval(size / 5, size / 5, size / 3, size / 3);
			g2.drawLine(size / 2, size / 2, size * 4 / 5, size * 4 / 5);
		}

		private void trash(Graphics2D g2) {
			g2.drawRoundRect(size / 4, size / 3, size / 2, size / 2, 3, 3);
			g2.drawLine(size / 5, size / 3, size * 4 / 5, size / 3);
			g2.drawLine(size / 3, size / 4, size * 2 / 3, size / 4);
		}
	}

	static Icon diamond(Color color, int size) {
		return new Icon() {
			@Override
			public int getIconWidth() {
				return size;
			}

			@Override
			public int getIconHeight() {
				return size;
			}

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.translate(x, y);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				Path2D outer = new Path2D.Double();
				outer.moveTo(size * 0.50, size * 0.03);
				outer.lineTo(size * 0.93, size * 0.50);
				outer.lineTo(size * 0.50, size * 0.97);
				outer.lineTo(size * 0.07, size * 0.50);
				outer.closePath();
				g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 90));
				g2.fill(outer);
				g2.setColor(color);
				g2.draw(outer);
				AffineTransform old = g2.getTransform();
				g2.rotate(Math.toRadians(45), size / 2.0, size / 2.0);
				g2.draw(new RoundRectangle2D.Double(size * 0.34, size * 0.18, size * 0.32, size * 0.64, 6, 6));
				g2.setTransform(old);
				g2.dispose();
			}
		};
	}
}
