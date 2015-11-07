/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.simulator.device;

/**
 * A,B,C设备暂时默认不以任何属性进行区别，只是分为不同类别
 *
 * @author shilin
 */
public class DeviceA extends Device {

	private static final long serialVersionUID = 8294752736342171887L;

	public DeviceA() {
	}

	public DeviceA(String Did, String name, int allNum, int availableNum, Boolean isArriable) {
		super(Did, name, allNum, availableNum, isArriable);
	}
}
