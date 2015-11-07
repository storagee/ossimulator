package com.os.simulator.process;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JPanel;

/**
 * @author : kezhenxu
 * @time : Nov 6, 2015 9:11:24 AM
 */
public class ProcessPanel extends JPanel implements Observer {

	private static final long	serialVersionUID	= 3859222666012952336L;

	private static int			Y_COORDINATE		= 36;
	private static int			X_COORDINATE		= 72;

	private ProcessManager		processManager;

	public ProcessPanel() {
		super();
		setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int line = 0;

		Graphics2D graphics2d = (Graphics2D) g;

		if (processManager == null) {
			return;
		}

		graphics2d.setColor(Color.BLACK);
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		LinkedBlockingQueue<Process> readyQueue = processManager.getReadyQueue();
		LinkedBlockingQueue<Process> blockQueue = processManager.getBlockedQueue();
		Process running = processManager.getRunning();

		graphics2d.drawString("正在运行：", X_COORDINATE, Y_COORDINATE * ++line);
		if (running != null) {
			graphics2d.drawString(running.getPID(), X_COORDINATE * 2, Y_COORDINATE * line);
		}

		graphics2d.drawString("就绪队列：", X_COORDINATE, Y_COORDINATE * ++line);

		int i$ = 2;
		for (Process p : readyQueue) {
			graphics2d.drawString(p.getPID(), X_COORDINATE * i$++, Y_COORDINATE * line);
		}

		graphics2d.drawString("阻塞队列：", X_COORDINATE, Y_COORDINATE * ++line);

		i$ = 2;
		for (Process p : blockQueue) {
			graphics2d.drawString(p.getPID(), X_COORDINATE * i$++, Y_COORDINATE * line);
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ProcessManager) {
			processManager = (ProcessManager) o;
		}
		repaint();
	}

}
