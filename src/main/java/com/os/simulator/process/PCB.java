package com.os.simulator.process;

import com.os.simulator.memory.MemBlock;
import com.os.simulator.memory.MemoryManager;

import java.util.HashMap;
import java.util.Map;

public class PCB {

	private String   pid;
	private MemBlock memBlock;
	private Integer  priority;
	private Integer  time;

	private Map<Integer, Byte[]> regs = new HashMap<>(8, 1);

	private PCB() {
		for (int i$ = 0; i$ < 8; i$++) {
			regs.put(i$, new Byte[32]);
		}
	}

	public String getPID() {
		return pid;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(final Integer time) {
		this.time = time;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setRegs(final Map<Integer, Byte[]> regs) {
		this.regs = regs;
	}

	public Map<Integer, Byte[]> getRegs() {
		return regs;
	}

	public MemBlock getMemBlock() {
		return memBlock;
	}

	@Override
	public String toString() {
		return "PCB{" +
		       "pid=" + pid +
		       ", priority=" + priority +
		       ", mem=" + memBlock +
		       '}';
	}

	public static final class PCBBuilder {

		private PCB pcb;

		public PCBBuilder() {
			pcb = new PCB();
		}

		public PCBBuilder pid(String pid) {
			pcb.pid = pid;
			return this;
		}

		public PCBBuilder time(Integer time) {
			if (time > 0) {
				pcb.time = time;
				return this;
			}
			throw new IllegalArgumentException("Time must be positive, you pass in " + time);
		}

		public PCBBuilder priority(Integer priority) {
			if (priority >= 1 && priority <= 10) {
				pcb.priority = priority;
				return this;
			}
			throw new IllegalArgumentException("Priority must between 1 and 10, you pass in " + priority);
		}

		public PCBBuilder memory(int memSize) throws Exception {
			MemBlock memBlock = MemoryManager.instance().allocate(memSize);
			pcb.memBlock = memBlock;
			return this;
		}

		public PCB build() {
			return pcb;
		}
	}
}
