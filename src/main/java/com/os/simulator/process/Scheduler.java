package com.os.simulator.process;

import java.util.Queue;

public class Scheduler implements TimeSliceEndListener {

	private static final Long	TIME_SLICE	= 3000L;

	private ProcessManager		processManager;

	public Scheduler(final ProcessManager iProcessManager) {
		this.processManager = iProcessManager;
	}

	public Process next() {
		if (processManager.getReadyQueue().isEmpty()) {
			return ProcessManager.DUMMY;
		}
		return processManager.getReadyQueue().poll();
	}

	public void run() {
		new SchedulerTask(this).start();
	}

	@Override
	public void onTimeSliceEnd() {

		final Process running = processManager.getRunning();
		final Queue<Process> readyQueue = processManager.getReadyQueue();
		if (readyQueue.isEmpty()) {
			if (running == null) {
				processManager.wakeUpProcess(ProcessManager.DUMMY);
			}
			return;
		}
		if (running == ProcessManager.DUMMY) {
			ProcessManager.DUMMY.block();
		}
		if (running != null && running != ProcessManager.DUMMY) {
			processManager.readyProcess(running);
		}
		processManager.wakeUpProcess(readyQueue.poll());
	}

	static class SchedulerTask extends Thread {

		private TimeSliceEndListener timeSliceEndListener;

		public SchedulerTask(final TimeSliceEndListener timeSliceEndListener) {
			this.timeSliceEndListener = timeSliceEndListener;
		}

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(TIME_SLICE);
					timeSliceEndListener.onTimeSliceEnd();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
