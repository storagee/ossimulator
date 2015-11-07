/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.simulator.device;

/**
 *
 * @author shilin
 */
public class DeviceC extends Device {

	private static final long serialVersionUID = -8234697909437796529L;

	public DeviceC() {
	}

	public DeviceC(String Did, String name, int allNum, int availableNum, Boolean isArriable) {
		super(Did, name, allNum, availableNum, isArriable);
	}

}
