/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.simulator.device;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import com.os.simulator.process.Process;

/**
 * @author shilin
 */
public class DevicePanel extends JPanel implements Observer {

	private static final long	serialVersionUID	= 8855288741973681791L;

	private DeviceManager		deviceManager		= DeviceManager.instance();

	public DevicePanel() {
		super();
		setLayout(null);
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D graphics2d = (Graphics2D) g;
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Device device = new Device();
		DeviceA A = new DeviceA();
		DeviceB B = new DeviceB();
		DeviceC C = new DeviceC();
		// setBackground(new Color(255, 255, 255));
		int width = 345, height = 170;
		int x = 20, y = 20;
		// graphics2d.setColor(new Color(255, 255, 255));
		// graphics2d.fillRect(x, y, width, height);
		graphics2d.setColor(Color.RED);
		double a1 = 344 * deviceManager.percentOfBeUsedDeviceA();
		int a = (int) a1;
		graphics2d.fillRect(21, 30, a, 30);
		System.out.println("申请后DeviceA" + DeviceManager.A.getAvailableNum());
		System.out.println(deviceManager.percentOfBeUsedDeviceA());

		graphics2d.setColor(Color.YELLOW);
		double b1 = 172 * DeviceManager.B.getAvailableNum();
		int b = (int) b1;
		graphics2d.fillRect(21, 85, b, 30);
		System.out.println("申请后DeviceB" + DeviceManager.B.getAvailableNum());

		System.out.println(deviceManager.percentOfBeUsedDeviceB());

		graphics2d.setColor(Color.GREEN);
		double c1 = 172 * DeviceManager.C.getAvailableNum();
		int c = (int) c1;
		graphics2d.fillRect(21, 140, c, 30);
		System.out.println("申请后DeviceC" + DeviceManager.C.getAvailableNum());
		System.out.println(deviceManager.percentOfBeUsedDeviceC());

		graphics2d.setColor(Color.BLACK);
		graphics2d.drawString("DeviceA", 30, 45);
		graphics2d.drawString("DeviceB", 30, 100);
		graphics2d.drawString("DeviceC", 30, 155);
		if ((1.0 * DeviceManager.A.getAvailableNum() * 100 / 2) > 0) {
			graphics2d.drawString("%" + 100.0, 180, 45);
		}
		else {
			graphics2d.drawString("%" + 0.0, 180, 45);
		}

		if ((1.0 * DeviceManager.B.getAvailableNum() * 100 / 2) > 0) {
			graphics2d.drawString("%" + 1.0 * DeviceManager.B.getAvailableNum() * 100 / 2, 180, 100);
		}
		else {
			graphics2d.drawString("%" + 0, 180, 45);
		}

		if ((1.0 * DeviceManager.C.getAvailableNum() * 100 / 2) > 0) {
			graphics2d.drawString("%" + 1.0 * DeviceManager.C.getAvailableNum() * 100 / 2, 180, 155);
		}
		else {
			graphics2d.drawString("%" + 0, 180, 45);
		}

		graphics2d.drawString("共一台", 310, 45);
		graphics2d.drawString("共两台", 310, 100);
		graphics2d.drawString("共两台", 310, 155);

		graphics2d.setColor(Color.BLUE);
		graphics2d.drawString("waiting:", 30, 75);
		if (deviceManager.waitingForDevice(A) != null) {
			for (int i = 0; i < deviceManager.waitingForDevice(A).size(); i++) {
				if (deviceManager.waitingForDevice(A).get(i) != null) {
					Process process = deviceManager.waitingForDevice(A).get(i);
					graphics2d.drawString(process.getPID(), 80 + 30 * i, 75);
				}
			}
		}
		graphics2d.drawString("waiting:", 30, 130);
		if (deviceManager.waitingForDevice(B) != null) {
			for (int i = 0; i < deviceManager.waitingForDevice(B).size(); i++) {
				if (deviceManager.waitingForDevice(B).get(i) != null) {
					Process process = deviceManager.waitingForDevice(B).get(i);
					System.out.println("nameB=" + process);
					graphics2d.drawString(process.getPID(), 80 + 30 * i, 130);
				}
			}
		}
		graphics2d.drawString("waiting:", 30, 185);
		if (deviceManager.waitingForDevice(C) != null) {
			for (int i = 0; i < deviceManager.waitingForDevice(C).size(); i++) {
				if (deviceManager.waitingForDevice(C).get(i) != null) {
					Process process = deviceManager.waitingForDevice(C).get(i);
					graphics2d.drawString(process.getPID(), 80 + 30 * i, 185);
				}
			}
		}
	}
}
