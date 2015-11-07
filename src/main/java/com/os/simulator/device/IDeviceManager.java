package com.os.simulator.device;

import java.io.Serializable;
import java.util.List;

public interface IDeviceManager extends  Serializable {

	/**
	 * 获取所有的设备(无论是否空闲)
	 *
	 * @return 所有的设备
	 */
	List<IDevice> getAllDevices();

	/**
	 * 申请使用设备
	 *
	 * @param device  要申请的设备
	 * @param process 申请的进程
	 *
	 * @return 成功返回 true, 否则 false
	 *
	 * @throws Exception 其他错误抛出异常
	 */
	boolean applyDevice(IDevice device, Process process) throws Exception;

	/**
	 * 释放使用的设备
	 *
	 * @param device  要释放的设备
	 * @param process 释放设备的进程
	 *
	 * @throws Exception 其他错误抛出异常
	 */
	void releaseDevice(IDevice device, Process process) throws Exception;

	/**
	 * 获取正在等待设备的进程
	 *
	 * @param device
	 *
	 * @return 正在等待设备的进程列表
	 *
	 * @throws Exception 其他错误抛出异常
	 */
	List<String> waitingForDevice(IDevice device) throws Exception;

	/**
	 * 显示设备是否可用
	 *
	 * @param device 要测试的设备
	 *
	 * @return 返回 device 表示的设备是否可用, 可用为 true, 否则 false
	 *
	 * @throws Exception 其他错误抛出异常
	 */
	boolean deviceAvailable(IDevice device) throws Exception;
}
