/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.simulator.memory;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.os.simulator.process.Process;
import com.os.simulator.process.ProcessManager;

/**
 * @author mosi
 */
public class MainFrame extends JFrame implements Observer {

	private MemoryPanel mempanel;

	public MainFrame() {
		this.mempanel = new MemoryPanel();
		add(mempanel);
	}

	public static MainFrame showMemPanel() {
		MainFrame frame = new MainFrame();
		frame.setSize(600, 250);
		frame.setTitle("Memory");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		return frame;
	}

	public static void main(String[] args) throws Exception { // 测试主方法
		MainFrame mainframe = showMemPanel();
		MemoryManager m = MemoryManager.instance();
		m.addObserver(mainframe);
		try {
			final ProcessManager manager = ProcessManager.instance();
			manager.run();
			System.out.println("manager.getRunning() = " + manager.getRunning());
			final Process process0 = manager.createProcess(21, 6);
			System.out.println("process0 = " + process0);
			final Process process1 = manager.createProcess(32, 8);
			System.out.println("process1 = " + process1);
			Thread.sleep(15000);

			final Process process2 = manager.createProcess(23, 10);
			System.out.println("process2 = " + process2);
			// final Process process3 = manager.createProcess(23, 15);
			// System.out.println("process3 = " + process3);
			// final Process process4 = manager.createProcess(23, 7);
			// System.out.println("process4 = " + process4);
			// final Process process5 = manager.createProcess(23, 5);
			// System.out.println("process5 = " + process5);
			// final Process process6 = manager.createProcess(23, 8);
			// System.out.println("process6 = " + process6);
			// final Process process7 = manager.createProcess(23, 10);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// MemBlock m1 = m.allocate(50);
		// Thread.sleep(2000);
		// MemBlock m2 = m.allocate(62);
		// Thread.sleep(2000);
		// MemBlock m3 = m.allocate(100);
		// Thread.sleep(2000);
		// MemBlock m4 = m.allocate(150);
		// Thread.sleep(2000);
		// m.free(m2);
		// Thread.sleep(2000);
		// MemBlock m5 = m.allocate(70); //FF算法分配测试
		// Thread.sleep(2000);
		// MemBlock m6 = m.allocate(80);
		// Thread.sleep(2000);
		// MemBlock m7 = m.allocate(63); //超出内存大小测试
		// System.out.println(m7);
		// Thread.sleep(2000);
		// MemBlock m8 = m.allocate(62); //申请内存大于总空闲内存，失败
		// Thread.sleep(2000);
		// m.free(m3);
		// Thread.sleep(2000);
		// m.free(m5);
		// MemBlock m13 = m.allocate(101); //申请的块大小大于任意一块，失败
		// Thread.sleep(2000);
		// System.out.println(m13);
		// Thread.sleep(2000);
		// m.free(m4); //前后三块空闲区合并测试
		// Thread.sleep(2000);
		// MemBlock m9 = m.allocate(200);
		// Thread.sleep(2000);
		// MemBlock m10 = m.allocate(50);
		// Thread.sleep(2000);
		// MemBlock m11 = m.allocate(70);
		// Thread.sleep(2000);
		// MemBlock m12 = m.allocate(11); //满负荷申请内存，失败
		// System.out.println(m12);
		// Thread.sleep(2000);
		// m.free(m11);
		// Thread.sleep(2000);
		// m.free(m6); //与前一块空闲区合并测试
		// Thread.sleep(2000);
		// m.free(m8);
		// Thread.sleep(2000);
		// m.free(m1); //与后一块空闲区合并测试
		// Thread.sleep(2000);
		// m.free(m9);
		// Thread.sleep(2000);
		// m.free(m10); //内存回收完毕
	}

	@Override
	public void update(Observable o, Object o1) {
		this.mempanel.repaint(); // 实时更新界面
	}

}
