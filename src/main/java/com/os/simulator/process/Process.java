package com.os.simulator.process;

import java.util.Observable;

import com.os.simulator.memory.MemoryManager;

public class Process extends Observable implements Comparable<Process> {

	private volatile State	state;
	private volatile PCB	pcb;

	private Process() {
	}

	static Process create(final PCB pcb) {
		final Process process = new Process();
		process.pcb = pcb;
		process.state = State.CREATED;
		return process;
	}

	PCB getPCB() {
		return pcb;
	}

	void block() {
		state = State.BLOCKED;
		setChanged();
		notifyObservers();
	}

	void run() {
		state = State.RUN;
		setChanged();
		notifyObservers();
		new Thread(() -> {
			while (state == State.RUN) {
				if (pcb.getTime() == 0 && !pcb.getPID().equals("DUMMY")) {
					ProcessManager.instance().destroyProcess(this);
				}
				System.out.println("Process " + pcb.getPID() + " is running, time remaining " + pcb.getTime());
				try {
					Thread.sleep(500);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				pcb.setTime(pcb.getTime() - 1);
			}
		}).start();
	}

	void ready() {
		state = State.READY;
		setChanged();
		notifyObservers();
	}

	void destroy() {
		state = State.DESTROYED;
		final MemoryManager memoryManager = MemoryManager.instance();
		try {
			memoryManager.free(pcb.getMemBlock());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers();
	}

	public String getPID() {
		return pcb.getPID();
	}

	public State getState() {
		return state;
	}

	@Override
	public int compareTo(final Process o) {
		return pcb.getPriority() - o.pcb.getPriority();
	}

	@Override
	public String toString() {
		return "Process{"+
				"state=" + state +
				", pcb=" + pcb +
				'}';
	}
}
