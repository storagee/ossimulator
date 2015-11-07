package com.os.simulator.file;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Created by Administrator on 2015/10/30.
 */
public class BlockDisplayPanel extends JPanel {

	private static final long	serialVersionUID	= 6227156623779414099L;
	private final static int	WIDTH				= 26;
	private final static int	HEIGHT				= 15;
	private final static int	MARGIN				= 1;
	private byte[]				fatContent;

	public BlockDisplayPanel(byte[] fatContent) {
		this.fatContent = fatContent;
		this.setBackground(Color.white);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int top = MARGIN;
		int temp = 0;
		Color folderColor = new Color(251, 170, 22);
		Color fileColor = new Color(75, 178, 36);
		Color emptyColor = new Color(216, 216, 216);
		for (int i = 0; i < this.fatContent.length; i++) {
			if (((i + 1) % 16) == 0) {
				temp = top;
				top = top + HEIGHT + MARGIN;

				if (i <= 1) {
					// 系统 红色
					g.setColor(Color.MAGENTA);
					g.fillRect((i % 16) * (WIDTH + MARGIN) + MARGIN, temp, WIDTH, HEIGHT);
				}
				else if (this.fatContent[i] == (byte) -1) {
					// 已用 文件
					g.setColor(fileColor);
					g.fillRect((i % 16) * (WIDTH + MARGIN) + MARGIN, temp, WIDTH, HEIGHT);
				}
				else if (this.fatContent[i] == (byte) -2) {
					// 已用 文件夹
					g.setColor(folderColor);
					g.fillRect((i % 16) * (WIDTH + MARGIN) + MARGIN, temp, WIDTH, HEIGHT);
				}
				else {
					// 未使用 灰色
					g.setColor(emptyColor);
					g.fillRect((i % 16) * (WIDTH + MARGIN) + MARGIN, temp, WIDTH, HEIGHT);
				}
			}
			else {
				if (i <= 1) {
					// 系统 红色
					g.setColor(Color.magenta);
					g.fillRect((i % 16) * (WIDTH + MARGIN) + MARGIN, top, WIDTH, HEIGHT);
				}
				else if (this.fatContent[i] == (byte) -1) {
					// 已用 文件
					g.setColor(fileColor);
					g.fillRect((i % 16) * (WIDTH + MARGIN) + MARGIN, top, WIDTH, HEIGHT);
				}
				else if (this.fatContent[i] == (byte) -2) {
					// 已用 文件夹
					g.setColor(folderColor);
					g.fillRect((i % 16) * (WIDTH + MARGIN) + MARGIN, top, WIDTH, HEIGHT);
				}
				else {
					// 未使用 灰色
					g.setColor(emptyColor);
					g.fillRect((i % 16) * (WIDTH + MARGIN) + MARGIN, top, WIDTH, HEIGHT);
				}
			}
		}
	}
}
