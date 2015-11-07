package com.os.simulator.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2015/10/23.
 */
public class Fat {

    public static DirEntry dirEntryForCmd;

    public static Boolean isCopy;

    public static String copyPath = "";

    public static String folderPath = "";

    private byte[] fatContent;

    public Fat(){
    }

    public Fat(byte[] fatContent) {
        this.fatContent = fatContent;
    }

    public byte[] getFatContent() {
        return fatContent;
    }

    public void setFatContent(byte[] fatContent) {
        this.fatContent = fatContent;
    }

    public byte allocate(Boolean isFile){
        //返回可用的盘块，并分配盘块
        for(int i=3; i<fatContent.length; i++){
            if(fatContent[i] == 0){
                byte[] bytes = new byte[1];
                if(isFile){
                    bytes[0] = (byte)-1;
                }else{
                    bytes[0] = (byte)-2;
                }
                fatContent[i] = bytes[0];
                try {
                    DAO.writeToDisk(bytes, i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return (byte)i;
            }
        }
        return (byte)-1;
    }

    public boolean delete(int index){
        //根据给定的下标，将指定盘块置零
        if(fatContent[index] != 0 && index != 0 && index != 1 && index != 2){
            //如果未分配、且不是文件配置表、FAT，则可以删除
            fatContent[index] = 0;
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<Byte> getFileBlocks(byte index){
        //根据传入的文件其实盘块，返回该文件的所有盘块
        ArrayList<Byte> fileBlocks = new ArrayList<Byte>();
        return fileBlocks;
    }

    public void clear(byte blockIndex){
        if(this.fatContent[blockIndex] == (byte)-1 || this.fatContent[blockIndex] == (byte) -2){
            byte[] bytes = new byte[1];
            bytes[0] = (byte) 0;
            try {
                DAO.writeToDisk(bytes, blockIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fatContent[blockIndex] = 0;
        }
    }
    @Override
    public String toString() {
        return "Fat{" +
                "fatContent=" + Arrays.toString(fatContent) +
                '}';
    }
}
