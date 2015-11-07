package com.os.simulator;

import java.awt.GridLayout;

import javax.swing.JFrame;

import com.os.simulator.device.DeviceManager;
import com.os.simulator.device.DevicePanel;
import com.os.simulator.memory.MemoryManager;
import com.os.simulator.memory.MemoryPanel;
import com.os.simulator.process.Process;
import com.os.simulator.process.ProcessManager;
import com.os.simulator.process.ProcessPanel;

public class OSSimulator extends JFrame {

	private static final long	serialVersionUID	= -3815686899094946982L;

	private ProcessManager		processManager		= ProcessManager.instance();
	private DeviceManager		deviceManager		= DeviceManager.instance();
	private MemoryManager		memoryManager		= MemoryManager.instance();

	private DevicePanel			devicePanel			= new DevicePanel();
	private MemoryPanel			memoryPanel			= new MemoryPanel();
	private ProcessPanel		processPanel		= new ProcessPanel();

	public OSSimulator() {
		setTitle("OS Simulator");
		setLayout(new GridLayout(2, 2));
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		add(processPanel);
		add(devicePanel);
		add(memoryPanel);

		deviceManager.addObserver(devicePanel);
		memoryManager.addObserver(memoryPanel);
		processManager.addObserver(processPanel);

		run();
	}

	private void run() {
		new Thread(() -> {
			try {
				processManager.run();

				Process p1 = processManager.createProcess(16, 10);
				Thread.sleep(2000);
				Process p2 = processManager.createProcess(36, 20);
				Thread.sleep(2000);
				deviceManager.applyDevice(DeviceManager.A, p1);
				Process p3 = processManager.createProcess(64, 16);
				Thread.sleep(2000);
				deviceManager.releaseDevice(DeviceManager.A, p1);
				deviceManager.applyDevice(DeviceManager.B, p2);
				Thread.sleep(3000);
				deviceManager.applyDevice(DeviceManager.B, p3);
				Thread.sleep(1000);
				Thread.sleep(3000);
				deviceManager.releaseDevice(DeviceManager.B, p3);
				deviceManager.releaseDevice(DeviceManager.B, p2);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

}
