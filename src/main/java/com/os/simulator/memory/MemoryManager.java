/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.simulator.memory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;

/**
 * @author mosi
 */
public final class MemoryManager extends Observable {

	private Color[]		colors	= {
										Color.BLUE,
										Color.GREEN,
										Color.DARK_GRAY,
										Color.RED,
										Color.PINK,
										Color.ORANGE,
										Color.YELLOW,
										Color.CYAN,
										Color.GRAY,
										Color.MAGENTA,
										Color.black,
										Color.LIGHT_GRAY
								};

	private static int	count	= 0;

	static class Singleton {
		static MemoryManager MEMORY_MANAGER = new MemoryManager();
	}

	private MemoryManager() {
		InitArrayList();
	}

	public static MemoryManager instance() {
		return Singleton.MEMORY_MANAGER;
	}

	public void InitArrayList() {
		MemBlock block = new MemBlock(0, 512);
		MemList.OCCUPY_LIST = new ArrayList<>();
		MemList.EMPTY_LIST = new ArrayList<>();
		MemList.EMPTY_LIST.add(block);
	}

	public MemBlock allocate(int size) throws Exception {
		if (size <= MemList.sumOfEmptySize()) {
			int i;
			int addr;
			for (i = 0; i < MemList.EMPTY_LIST.size(); i++) {
				if (size <= MemList.EMPTY_LIST.get(i).getSize()) {
					if (size == MemList.EMPTY_LIST.get(i).getSize()) { // 刚好找到一块大小与需求相等的空闲块
						addr = MemList.EMPTY_LIST.get(i).getAddress();
						MemBlock block = new MemBlock(addr, size);
						block.setColor(colors[count]);
						count++;
						if (count == colors.length) {
							count = 0;
						}
						MemList.OCCUPY_LIST.add(block);
						MemList.EMPTY_LIST.remove(i);
						setChanged();
						notifyObservers();
						return new MemBlock(addr, size);
					}
					else {
						addr = MemList.EMPTY_LIST.get(i).getAddress(); // 申请的空间需要分割已有空闲块
						MemBlock block = new MemBlock(addr, size);
						block.setColor(colors[count]);
						count++;
						if (count == colors.length) {
							count = 0;
						}
						MemList.OCCUPY_LIST.add(block);
						MemList.EMPTY_LIST.get(i).setAddress(addr + size);
						int emptysize = MemList.EMPTY_LIST.get(i).getSize();
						MemList.EMPTY_LIST.get(i).setSize(emptysize - size);
						setChanged();
						notifyObservers();
						return new MemBlock(addr, size);
					}
				}
			}
		}
		throw new IllegalArgumentException("Can not allocate memory, too large: " + size);
	}

	public void free(MemBlock location) throws Exception {
		int i;
		int j;
		out: for (i = 0; i < MemList.OCCUPY_LIST.size(); i++) {
			if (location.getAddress() == MemList.OCCUPY_LIST.get(i).getAddress()) {
				MemBlock block = MemList.OCCUPY_LIST.get(i);
				block.setColor(null);
				MemList.OCCUPY_LIST.remove(block); // 找到并将块从占用线性表中释放
				int addrnsize = block.getAddress() + block.getSize();
				for (j = 0; j < MemList.EMPTY_LIST.size(); j++) {
					if (location.getAddress() < MemList.EMPTY_LIST.get(j).getAddress()) {
						MemList.EMPTY_LIST.add(j, block); // 将释放的空闲块按地址高低插入空闲线性表
						if (j == 0) { // 若插入在表首，检查是否能与第二块合并
							if (addrnsize == MemList.EMPTY_LIST.get(j + 1).getAddress()) {
								MemList.EMPTY_LIST.get(j)
										.setSize(block.getSize() + MemList.EMPTY_LIST.get(j + 1).getSize());
								MemList.EMPTY_LIST.remove(j + 1);
								break out;
							}
							break out;
						}
						int former = MemList.EMPTY_LIST.get(j - 1).getAddress()
								+ MemList.EMPTY_LIST.get(j - 1).getSize(); // 前一个空闲块的地址与大小之和
						if ((former == block.getAddress())
								&& (addrnsize == MemList.EMPTY_LIST.get(j + 1).getAddress())) {// 在表中插入，检查前后是否能够同时合并
							MemList.EMPTY_LIST.get(j - 1).setSize(block.getSize()
									+ MemList.EMPTY_LIST.get(j - 1).getSize() + MemList.EMPTY_LIST.get(j + 1)
											.getSize());
							MemList.EMPTY_LIST.remove(j + 1);
							MemList.EMPTY_LIST.remove(j);
							break out;
						}
						else if (former == block.getAddress()) { // 在表中插入，只能与前者合并
							MemList.EMPTY_LIST.get(j - 1)
									.setSize(block.getSize() + MemList.EMPTY_LIST.get(j - 1).getSize());
							MemList.EMPTY_LIST.remove(j);
							break out;
						}
						else if (addrnsize == MemList.EMPTY_LIST.get(j + 1).getAddress()) { // 在表中插入，只能与后者合并
							MemList.EMPTY_LIST.get(j)
									.setSize(block.getSize() + MemList.EMPTY_LIST.get(j + 1).getSize());
							MemList.EMPTY_LIST.remove(j + 1);
							break out;
						}
						break out;
					}
				}
				MemList.EMPTY_LIST.add(block); // 将空闲块插至线性表最后
				j = MemList.EMPTY_LIST.size();
				if (j > 1) {
					if ((MemList.EMPTY_LIST.get(j - 2).getAddress() + MemList.EMPTY_LIST.get(j - 2).getSize()) == block
							.getAddress()) { // 如果空闲块与前一块能合并就合并
						int newsize = MemList.EMPTY_LIST.get(j - 2).getSize() + block.getSize();
						MemList.EMPTY_LIST.get(j - 2).setSize(newsize);
						MemList.EMPTY_LIST.remove(j - 1);
						break;
					}
				}
				break;
			}
		}
		setChanged();
		notifyObservers();
	}
}
