package com.os.simulator.file;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/10/26.
 */
public class Disk {
    private Block[] blocks;
    private Fat fat;

    public Disk() {
    }

    public Disk(Block[] blocks, Fat fat) {
        this.blocks = blocks;
        this.fat = fat;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[] blocks) {
        this.blocks = blocks;
    }

    public Fat getFat() {
        return fat;
    }

    public void setFat(Fat fat) {
        this.fat = fat;
    }

    public Block getBlockByIndex(byte index){
        return this.blocks[index];
    }

    public Block getRoot(){
        return this.blocks[2];
    }
}
