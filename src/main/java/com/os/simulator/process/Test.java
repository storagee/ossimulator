package com.os.simulator.process;

public class Test {

	public static void main(String[] args) {
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

		// System.out.println("process7 = " + process7);
		// final Process process8 = manager.createProcess();
		// System.out.println("process8 = " + process8);
		// final Process process9 = manager.createProcess();
		// System.out.println("process9 = " + process9);
	}
}
