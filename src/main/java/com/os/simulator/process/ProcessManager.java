package com.os.simulator.process;

import java.util.Map;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

import com.os.simulator.process.PCB.PCBBuilder;

public final class ProcessManager extends Observable {

	public static final Integer	DEFAULT_PRIORITY	= 5;

	public static final Process	DUMMY;

	static {
		DUMMY = Process.create(new PCBBuilder()	.pid("DUMMY")
												.time(Integer.MAX_VALUE)
												.build());
	}

	private LinkedBlockingQueue<Process>	blockedQueue	= new LinkedBlockingQueue<>(10);
	private LinkedBlockingQueue<Process>	readyQueue		= new LinkedBlockingQueue<>(10);

	private Scheduler						scheduler;
	private Process							running;
	private Map<Integer, Byte[]>			regs;

	private Long							currentPid		= 0L;

	private ProcessManager() {
		scheduler = new Scheduler(this);
		DUMMY.run();
		running = DUMMY;
	}

	static class Singleton {
		static ProcessManager PROCESS_MANAGER = new ProcessManager();
	}

	public static ProcessManager instance() {
		return Singleton.PROCESS_MANAGER;
	}

	public Process createProcess(int memSize, int time) throws Exception {
		final PCB pcb = new PCB.PCBBuilder()
											.pid(String.valueOf(currentPid++))
											.priority(DEFAULT_PRIORITY)
											.memory(memSize)
											.time(time)
											.build();
		final Process process = Process.create(pcb);
		process.ready();

		readyQueue.add(process);
		setChanged();
		notifyObservers();
		return process;
	}

	public Process getRunning() {
		return running;
	}

	public void destroyProcess(final Process process) {
		if (running == process) {
			running = null;
		}
		removeProcess(process);
		process.destroy();
		setChanged();
		notifyObservers();
	}

	public void blockProcess(final Process process) {
		if (running == process) {
			running = null;
		}
		removeProcess(process);
		process.getPCB().setRegs(regs);
		process.block();
		blockedQueue.add(process);
		setChanged();
		notifyObservers();
	}

	public void readyProcess(final Process process) {
		if (running == process) {
			running = null;
		}
		removeProcess(process);
		process.ready();
		readyQueue.add(process);
		setChanged();
		notifyObservers();
	}

	public void wakeUpProcess(final Process process) {
		removeProcess(process);
		regs = process.getPCB().getRegs();
		process.run();
		running = process;
		setChanged();
		notifyObservers();
	}

	public void run() {
		scheduler.run();
	}

	public LinkedBlockingQueue<Process> getBlockedQueue() {
		return blockedQueue;
	}

	public LinkedBlockingQueue<Process> getReadyQueue() {
		return readyQueue;
	}

	private void removeProcess(final Process process) {
		blockedQueue.remove(process);
		readyQueue.remove(process);
	}
}
