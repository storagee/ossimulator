package com.os.simulator.file;

import static com.os.simulator.file.Constants.DISK_DAT;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by LZH on 2015/10/23.
 */
public class DAO {

	public DAO() {}

	public Disk readDisk(String diskPath) {
		Disk disk = new Disk();
		File file = new File(diskPath);
		disk.setFat(createFat(file));
		disk.setBlocks(createBlocks(file));
		return disk;
	}

	private Fat createFat(File file) {
		byte[] fatContent = readBytes(file, (byte) 0, (byte) 127);
		Fat fat = new Fat();
		fat.setFatContent(fatContent);
		return fat;
	}

	private Block[] createBlocks(File file) {
		Block[] blocks = new Block[128];// 除了文件配置表(FAT),共有126个盘块
		byte[] bytesOfBlock = new byte[64];// 每个盘块有64个字节
		for (int i = 2; i < blocks.length; i++) {
			// 从第2个盘块到127个盘块
			bytesOfBlock = readBytes(file, 64 * i, 64 * (i + 1) - 1);
			blocks[i] = new Block();
			blocks[i].setDirEntries(createDirEntries(bytesOfBlock, 64 * i));
			blocks[i].setFileContent(createFileContent(bytesOfBlock));
		}
		return blocks;
	}

	private byte[] readBytes(File file, int beginIndex, int endIndex) {
		// 给定起始下标，结束下标，从disk.dat文件中读取字节，返回该区间的字节数组
		// 因为找不到适合的方法，磁盘较小，这里采用每次读取整个磁盘，再截取对应的字节数组的方法
		int size = endIndex - beginIndex + 1;
		byte[] diskBytes = new byte[8192];
		byte[] bytes = new byte[size];
		try (FileInputStream FIS = new FileInputStream(file)) {
			FIS.read(diskBytes, 0, diskBytes.length);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = beginIndex; i < endIndex + 1; i++) {
			bytes[i - beginIndex] = diskBytes[i];
		}
		return bytes;
	}

	private ArrayList<DirEntry> createDirEntries(byte[] bytesOfBlock, int beginIndex) {
		// 给定一个块的所有字节，以目录项的形式解析，返回所有的目录项/空的也返回
		ArrayList<DirEntry> dirEntries = new ArrayList<>();
		for (int i = 0; i < bytesOfBlock.length; i += 8) {

			DirEntry dirEntry = new DirEntry();

			dirEntry.setBeginIndex(beginIndex + i);

			dirEntry.setName(""	+
								(char) bytesOfBlock[i] +
								(char) bytesOfBlock[i + 1] +
								(char) bytesOfBlock[i + 2] + "", true);

			dirEntry.setSuffix("" + (char) bytesOfBlock[i + 3] + (char) bytesOfBlock[i + 4] + "", true);
			dirEntry.setProperty(bytesOfBlock[i + 5], true);
			dirEntry.setBeginBlock(bytesOfBlock[i + 6], true);
			dirEntry.setLength(bytesOfBlock[i + 7], true);
			dirEntries.add(dirEntry);
		}
		return dirEntries;
	}

	private char[] createFileContent(byte[] bytesOfBlock) {
		// 给定一个块所有的字节，以字符数组的形式解析，返回所有字符数组
		return new char[64];
	}

	public static void writeToDisk(byte[] bytes, int beginIndex) throws IOException {
		try (	FileInputStream FIS = new FileInputStream(DISK_DAT);
				FileOutputStream FOS = new FileOutputStream(DISK_DAT)) {
			byte[] diskBytes = new byte[8192];
			FIS.read(diskBytes, 0, diskBytes.length);
			FIS.close();
			for (int i = beginIndex; i < beginIndex + bytes.length; i++) {
				diskBytes[i] = bytes[i - beginIndex];
			}

			FOS.write(diskBytes, 0, diskBytes.length);
		}
	}

	public void clearDisk() {
		byte buffer[] = new byte[8192];
		buffer[0] = (byte) -1;// 文件结束
		buffer[1] = (byte) -1;
		buffer[2] = (byte) -2;
		// 创建文件输出流对象
		try (FileOutputStream FOS = new FileOutputStream(DISK_DAT)) {
			for (int i = 3; i < buffer.length; i++) {
				buffer[i] = (byte) 0;
			}
			FOS.write(buffer, 0, buffer.length);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("已格式化磁盘！");
	}

}
