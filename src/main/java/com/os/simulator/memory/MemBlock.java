/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.simulator.memory;

import java.awt.Color;

/**
 *
 * @author mosi
 */
public class MemBlock {
    private int address;                        
    private int size;
    private Color color;

    public MemBlock(int address, int size) {      //线性表内部数据结构存储内存块的地址和大小
        this.address = address;
        this.size = size;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

	@Override
	public String toString() {
		return "MemBlock{" +
		       "address=" + address +
		       ", size=" + size +
		       '}';
	}
}
