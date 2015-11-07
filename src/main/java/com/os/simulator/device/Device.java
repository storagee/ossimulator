/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.simulator.device;

/**
 * @author shilin
 * @设备名：name
 * @某设备总数：allNum
 * @某设备的编号：区别所有设备的唯一标
 * @具体某一个设备是否可用：isArriable
 */
public class Device implements IDevice {

	private static final long	serialVersionUID	= 5400304238372897381L;

	private String				Did;
	private String				name;
	private int					allNum;
	private int					availableNum;
	private Boolean				isArriable;

	public Device() {
	}

	public Device(String Did, String name, int allNum, int availableNum, Boolean isArriable) {
		this.Did = Did;
		this.name = name;
		this.allNum = allNum;
		this.availableNum = availableNum;
		this.isArriable = isArriable;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAllNum() {
		return allNum;
	}

	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}

	public Boolean getIsArriable() {
		return isArriable;
	}

	public void setIsArriable(Boolean isArriable) {
		this.isArriable = isArriable;
	}

	public String getDid() {
		return Did;
	}

	public void setDid(String Did) {
		this.Did = Did;
	}

	public int getAvailableNum() {
		return availableNum;
	}

	public void setAvailableNum(int availableNum) {
		this.availableNum = availableNum;
	}
}
