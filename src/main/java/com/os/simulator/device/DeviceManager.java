/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.simulator.device;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.os.simulator.process.Process;
import com.os.simulator.process.ProcessManager;

/**
 * @author shilin
 */
@SuppressWarnings({ "unused", "javadoc" })
public class DeviceManager extends Observable {

	private static List<Process>	waitingDevAList	= new ArrayList<>();
	private static List<Process>	waitingDevBList	= new ArrayList<>();
	private static List<Process>	waitingDevCList	= new ArrayList<>();

	public static DeviceA			A				= null;
	public static DeviceB			B				= null;
	public static DeviceC			C				= null;

	private ProcessManager			processManager	= ProcessManager.instance();

	static class Singleton {
		static DeviceManager DEVICE_MANAGER = new DeviceManager();
	}

	private DeviceManager() {
		initAllDevices();
	}

	public static DeviceManager instance() {
		return Singleton.DEVICE_MANAGER;
	}

	private void initAllDevices() {
		A = new DeviceA();
		A.setIsArriable(Boolean.TRUE);
		A.setAllNum(1);
		A.setAvailableNum(1);
		B = new DeviceB();
		B.setIsArriable(Boolean.TRUE);
		B.setAllNum(2);
		B.setAvailableNum(2);
		C = new DeviceC();
		C.setIsArriable(Boolean.TRUE);
		C.setAllNum(2);
		C.setAvailableNum(2);
		setChanged();
		notifyObservers();
	}

	public List<IDevice> getAllDevices() {
		List<IDevice> dList = new ArrayList<>();
		dList.add(A);
		dList.add(B);
		dList.add(C);
		return dList;
	}

	/**
	 * 这里只进行设备的释放，程序的销毁由进程进行 假设设备的申请放在List里顺序为A，B,C（1为要申请，0为不申请）
	 *
	 * @param device
	 * @param process
	 *
	 * @throws Exception
	 */
	public void releaseDevice(IDevice device, Process process) throws Exception {

		if (device instanceof DeviceA) {
			int availableNum = A.getAvailableNum() + 1;
			A.setAvailableNum(availableNum);
		}
		if (device instanceof DeviceB) {
			int availableNum = B.getAvailableNum() + 1;
			B.setAvailableNum(availableNum);
		}
		if (device instanceof DeviceC) {
			int availableNum = C.getAvailableNum() + 1;
			C.setAvailableNum(availableNum);
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * 释放某一个设备（可用设为true）
	 *
	 * @param device
	 */
	// public void releaseOneDevice(IDevice device) {
	// if (device instanceof DeviceA) {
	// int availableNum = A.getAvailableNum() + 1;
	// A.setAvailableNum(availableNum);
	// }
	// if (device instanceof DeviceB) {
	// int availableNum = B.getAvailableNum() + 1;
	// B.setAvailableNum(availableNum);
	// }
	// if (device instanceof DeviceB) {
	// int availableNum = B.getAvailableNum() + 1;
	// B.setAvailableNum(availableNum);
	// }
	// }
	public List<Process> waitingForDevice(IDevice device) {
		List<Process> devices = null;
		if (device instanceof DeviceA) {
			devices = waitingDevAList;
			// if (waitingDevAList != null) {
			// System.out.println("waitingDevAList" + waitingDevAList.get(0));
			// }
		}
		if (device instanceof DeviceB) {
			devices = waitingDevBList;
			// if (waitingDevBList != null) {
			// System.out.println("waitingDevAList" + waitingDevAList.get(0));
			// }
		}
		if (device instanceof DeviceC) {
			devices = waitingDevCList;
			// if (waitingDevCList != null) {
			// System.out.println("waitingDevAList" + waitingDevAList.get(0));
			// }
		}
		return devices;
	}

	public double percentOfBeUsedDeviceA() {
		int availableNumA;
		availableNumA = A.getAvailableNum();
		int percent;
		if ((availableNumA / 1) > 0) {
			percent = availableNumA / 1;
		}
		else {
			percent = 0;
		}
		return percent;
	}

	public double percentOfBeUsedDeviceB() {
		int availableNumB;
		availableNumB = B.getAvailableNum();
		int percent;
		if (((int) (1.0 * availableNumB / 2)) > 0) {
			percent = (int) (1.0 * availableNumB / 2);
		}
		else {
			percent = 0;
		}
		return percent;
	}

	public double percentOfBeUsedDeviceC() {
		int availableNumC;
		availableNumC = C.getAvailableNum();
		int percent;
		if (((int) (1.0 * availableNumC / 2)) > 0) {
			percent = (int) (1.0 * availableNumC / 2);
		}
		else {
			percent = 0;
		}
		return percent;
	}

	public boolean deviceAvailable(IDevice device) throws Exception {
		Boolean flag = true;
		if (device instanceof DeviceA) {
			if (!(A.getAvailableNum() > 0)) {
				flag = false;
			}
		}

		if (device instanceof DeviceB) {
			if (!(B.getAvailableNum() > 0)) {
				flag = false;
			}
		}

		if (device instanceof DeviceC) {
			if (!(C.getAvailableNum() > 0)) {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 申请一种设备 需判断进程申请时不会发生死锁. 如果申请成功（不死锁），把某种设备的可用数量减一，返回true,
	 * 如果申请不成功（死锁），返回false,并将申请该设备的进程放入进程等待队列
	 *
	 * @param device
	 * @param process
	 *
	 * @return flag
	 *
	 * @throws Exception
	 */
	public boolean applyDevice(IDevice device, Process process) throws Exception {
		Boolean flag = true;
		List<IDevice> devices = getAllDevices();
		if (deadlock(process)) {
			flag = false;
			if (device instanceof DeviceA) {
				waitingDevAList.add(process);
			}
			if (device instanceof DeviceB) {
				waitingDevBList.add(process);
				System.out.println("name=" + process);
			}
			if (device instanceof DeviceC) {
				waitingDevCList.add(process);
			}
		}
		else {
			if (device instanceof DeviceA) {
				int availableNum = A.getAvailableNum() - 1;
				System.out.println("a " + availableNum);
				A.setAvailableNum(availableNum);
			}
			if (device instanceof DeviceB) {
				int availableNum = B.getAvailableNum() - 1;
				B.setAvailableNum(availableNum);
			}
			if (device instanceof DeviceC) {
				int availableNum = C.getAvailableNum() - 1;
				C.setAvailableNum(availableNum);
			}
		}
		setChanged();
		notifyObservers();
		return flag;
	}

	/**
	 * 得到所有种类设备的可否使用情况
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public List<Integer> deviceIsAvailableList() throws Exception {
		List<Integer> list = new ArrayList<>();
		if (A.getAvailableNum() > 0) {
			list.add(1);
		}
		else {
			list.add(0);
		}
		if (B.getAvailableNum() > 0) {
			list.add(1);
		}
		else {
			list.add(0);
		}
		if (C.getAvailableNum() > 0) {
			list.add(1);
		}
		else {
			list.add(0);
		}
		return list;
	}

	/**
	 * 采用“资源静态分配法”判断是否死锁
	 *
	 * @param process
	 *
	 * @return true(死锁)，false（不死锁）
	 */
	public Boolean deadlock(Process process) throws Exception {
		DeviceA A = new DeviceA();
		DeviceB B = new DeviceB();
		DeviceC C = new DeviceC();
		List<Integer> applyList = new ArrayList<>();
		applyList.add(0);
		applyList.add(0);
		applyList.add(0);
		Boolean flag = true;
		List<Integer> availableList = deviceIsAvailableList();
		if ((availableList.get(0) >= applyList.get(0)) && (availableList.get(1) >= applyList
				.get(1)) && (availableList.get(2) >= applyList.get(2))) {
			flag = false;
		}
		System.out.println("compare" + availableList.get(0) + applyList.get(0));
		System.out.println("compare" + availableList.get(1) + applyList.get(1));
		System.out.println("compare" + availableList.get(2) + applyList.get(2));
		System.out.println(flag);
		return flag;
	}
}
