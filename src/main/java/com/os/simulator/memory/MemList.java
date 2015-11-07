/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.simulator.memory;

import java.util.ArrayList;

/**
 * @author mosi
 */
public class MemList {

	public static ArrayList<MemBlock> EMPTY_LIST;
	public static ArrayList<MemBlock> OCCUPY_LIST;

	public static int sumOfEmptySize() {
		int i;
		int size = 0;
		int j    = EMPTY_LIST.size();
		for (i = 0; i < j; i++) {
			size += EMPTY_LIST.get(i).getSize();
		}
		return size;
	}
}
