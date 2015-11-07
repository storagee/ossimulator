/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.simulator.device;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.os.simulator.process.Process;
import com.os.simulator.process.ProcessManager;

/**
 * @author shilin
 */
public class Main {

	public static void main(String args[]) throws Exception {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (ClassNotFoundException ex) {
			Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
		}

		EventQueue.invokeLater(() -> {
			JFrame frame = new JFrame();
			frame.setVisible(true);
			frame.setSize(400, 250);
			frame.setTitle("设备管理");
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			DevicePanel p = new DevicePanel();
			p.setSize(400, 250);
			p.setVisible(true);
			frame.add(p);
		});
		DeviceManager dm = DeviceManager.instance();
		System.out.println("初始A=" + dm.A.getAvailableNum());
		System.out.println("初始B=" + dm.B.getAvailableNum());
		Device A = new DeviceA();
		Device B = new DeviceB();
		Device C = new DeviceC();
		final ProcessManager processManager = ProcessManager.instance();
		Process p = processManager.createProcess(2, 10);
		Process p1 = processManager.createProcess(2, 10);
		Process p2 = processManager.createProcess(2, 10);
		Process p3 = processManager.createProcess(2, 10);
		Process p4 = processManager.createProcess(2, 10);
		Process p5 = processManager.createProcess(2, 10);
		Process p6 = processManager.createProcess(2, 10);
		Process p7 = processManager.createProcess(2, 10);

		dm.applyDevice(B, p);
		Thread.sleep(10000);
		dm.applyDevice(C, p1);
		Thread.sleep(10000);
		dm.applyDevice(A, p2);
		Thread.sleep(10000);
		dm.applyDevice(A, p5); // 死锁发生，p5进A设备等待队列
		Thread.sleep(10000);
		dm.releaseDevice(A, p2); // 进程p2释放A，p5出A设备等待队列并申请A
		Thread.sleep(10000);
		dm.releaseDevice(A, p5); // p5释放A
		Thread.sleep(10000);
		dm.applyDevice(B, p4);
		Thread.sleep(10000);
		dm.applyDevice(B, p3);
		Thread.sleep(10000);
		dm.releaseDevice(C, p1);
		Thread.sleep(10000);
		dm.applyDevice(B, p6); // 死锁发生，p6进B设备等待队列
		Thread.sleep(10000);
		dm.applyDevice(B, p7); // 死锁再次发生，p7进B设备等待队列
		Thread.sleep(10000);
		dm.releaseDevice(B, p); // 进程P释放B设备，P6出队，得到B
		Thread.sleep(10000);
		dm.releaseDevice(B, p4); // 进程P4释放B设备，P7出队列，得到B
		Thread.sleep(10000);
		dm.releaseDevice(B, p3);
		Thread.sleep(10000);
		dm.releaseDevice(B, p7);
		Thread.sleep(10000);
		dm.releaseDevice(B, p6);
	}
}
