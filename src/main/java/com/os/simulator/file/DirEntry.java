package com.os.simulator.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/10/23.
 */
public class DirEntry {

    private int beginIndex;
    private String name;
    private String suffix; //后缀
    private byte property; //属性
    private byte BeginBlock;
    private byte length;

    public DirEntry() {
    }

    public DirEntry(String name, String suffix, byte property, byte beginBlock, byte length) {
        this.name = name;
        this.suffix = suffix;
        this.property = property;
        BeginBlock = beginBlock;
        this.length = length;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name, boolean isInit) {
        this.name = name;
        if (!isInit) {
            byte[] bytes = name.getBytes();
            try {
                DAO.writeToDisk(bytes, this.beginIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix, boolean isInit) {
        this.suffix = suffix;
        if (!isInit) {
            byte[] bytes = suffix.getBytes();
            try {
                DAO.writeToDisk(bytes, beginIndex + 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte getProperty() {
        return property;
    }

    public void setProperty(byte property, boolean isInit) {
        this.property = property;
        if (!isInit) {
            byte[] bytes = new byte[1];
            bytes[0] = property;
            try {
                DAO.writeToDisk(bytes, this.beginIndex + 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte getBeginBlock() {
        return BeginBlock;
    }

    public void setBeginBlock(byte beginBlock, boolean isInit) {
        BeginBlock = beginBlock;
        if (!isInit) {
            byte[] bytes = new byte[1];
            bytes[0] = beginBlock;
            try {
                DAO.writeToDisk(bytes, this.beginIndex + 6);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte getLength() {
        return length;
    }

    public void setLength(byte length, boolean isInit) {
        this.length = length;
        if (!isInit) {
            byte[] bytes = new byte[1];
            bytes[0] = length;
            try {
                DAO.writeToDisk(bytes, this.beginIndex + 7);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isDir() {
        if (this.getProperty() == (byte) 8) {
            return true;
        }
        return false;
    }

    public void clear() {
        //clear property
        this.setProperty((byte) 0, false);
        //clear磁盘
        byte[] bytes = new byte[8];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) 0;
        }

        try {
            DAO.writeToDisk(bytes, beginIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        if (this.isDir()) {
            return this.getName();
        } else {
            return this.getName() + "." + this.getSuffix();
        }
    }

    public String showAllProperty(){
        return this.getName()+"."+this.getSuffix()+",属性:"+this.getProperty()
                +",起始盘快号:"+this.BeginBlock+",长度:"+this.getLength();
    }
}
