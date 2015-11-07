package com.os.simulator.file;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/23.
 */
public class Block {

	// 如果是文件夹，则有目录项
	ArrayList<DirEntry>	dirEntries;

	// 如果是文件，则有文件内容，这部分是扩充部分，用于文件读写
	char[]				fileContent;

	public Block() {}

	public Block(ArrayList<DirEntry> dirEntries, char[] fileContent) {
		this.dirEntries = dirEntries;
		this.fileContent = fileContent;
	}

	public ArrayList<DirEntry> getDirEntries() {
		return dirEntries;
	}

	public void setDirEntries(ArrayList<DirEntry> dirEntries) {
		this.dirEntries = dirEntries;
	}

	public char[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(char[] fileContent) {
		this.fileContent = fileContent;
	}

	public DirEntry getDirEntryByName(String name, Boolean isFile) {
		// 此方法不好，应该根据方法名区分
		for (int i = 0; i < dirEntries.size(); i++) {
			if (isFile) {
				if (dirEntries.get(i).getName().equals(name) && !dirEntries.get(i).isDir()) {
					return dirEntries.get(i);
				}
			}
			else {
				if (dirEntries.get(i).getName().equals(name) && dirEntries.get(i).isDir()) {
					return dirEntries.get(i);
				}
			}
		}
		return null;
	}

	public int getNumberOfEntry() {
		// 返回文件夹和文件的数目，目的：在界面展示该文件夹共有几个项目
		return 0;
	}

	public DirEntry allocateEntry() {
		// 返回空的目录项用于分配
		for (int i = 0; i < this.dirEntries.size(); i++) {
			if (this.dirEntries.get(i).getProperty() == 0) {
				return this.dirEntries.get(i);
			}
		}
		return null;
	}

	public ArrayList<DirEntry> getUsedDirEntry() {
		// 返回该盘块已经分配的所有目录项
		ArrayList<DirEntry> dirEntries = new ArrayList<>();
		for (int i = 0; i < this.dirEntries.size(); i++) {
			if (this.dirEntries.get(i).getProperty() > 0) {
				dirEntries.add(this.dirEntries.get(i));
			}
		}
		return dirEntries;
	}

	public void deleteFileEntryByName(String name) {
		for (int i = 0; i < this.getDirEntries().size(); i++) {
			if (!this.getDirEntries().get(i).isDir() && this.getDirEntries().get(i).getName().equals(name)) {
				this.getDirEntries().get(i).clear();
			}
		}
	}

	public void deleteEmptyFolderEntryByName(String name) {
		// 只是删除了目录项，所以叫Empty，其实不然
		for (int i = 0; i < this.getDirEntries().size(); i++) {
			if (this.getDirEntries().get(i).isDir() && this.getDirEntries().get(i).getName().equals(name)) {
				this.getDirEntries().get(i).clear();
			}
		}
	}
}
