package com.os.simulator.memory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class MemoryPanel extends JPanel implements Observer {

	private static final long	serialVersionUID	= -4572288932946252178L;

	private String[]			locations			= { "0", "128", "256", "384", "512" };

	public MemoryPanel() {
		super();
		setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g) { // 负责界面的绘画
		super.paintComponent(g);

		Graphics2D graphics2d = (Graphics2D) g;

		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.drawRect(40, 30, 512, 50);
		graphics2d.drawRect(40, 130, 512, 50);
		for (int i = 0; i < 5; i++) {
			graphics2d.drawString(locations[i], 128 * i + 40, 95);
		}

		if (MemList.OCCUPY_LIST.size() > 0) {
			for (int i = 0; i < MemList.OCCUPY_LIST.size(); i++) {
				graphics2d.setColor(MemList.OCCUPY_LIST.get(i).getColor());
				int x = MemList.OCCUPY_LIST.get(i).getAddress();
				int width = MemList.OCCUPY_LIST.get(i).getSize();
				graphics2d.fillRect(40 + x, 30, width, 50);
			}
			int occupySize = 512 - MemList.sumOfEmptySize();
			graphics2d.setColor(Color.red);
			graphics2d.fillRect(40, 130, occupySize, 50);
			double percent = occupySize * 1.0D / 512;
			graphics2d.drawString(String.format("%s %.0f%%", new Object[] { "内存占用比例：", percent * 100.0D }), 40, 200);
		}
		else {
			graphics2d.drawString("内存占用比例: 0%", 40, 200);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}
}
