package com.os.simulator.file;

import static com.os.simulator.file.Constants.DISK_DAT;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * Created by Administrator on 2015/10/23.
 */
public class FileManager extends Observable implements IFileManager {

	private Disk disk;

	public Disk getDisk() {
		return disk;
	}

	public void setDisk(Disk disk) {
		this.disk = disk;
	}

	DAO dao;

	public FileManager() {
		dao = new DAO();
		this.disk = dao.readDisk(DISK_DAT);
	}

	@Override
	public Boolean create(String fileName) throws Exception {
		Boolean isSuccess;
		isSuccess = createDirEntry(fileName, true);
		return isSuccess;
	}

	@Override
	public Boolean delete(String fileName) throws Exception {
		Boolean isSuccess;
		isSuccess = deleteDirEntry(fileName, true, false);
		return isSuccess;
	}

	@Override
	public String show(String fileName) throws Exception {
		// 显示文件
		return showFile(fileName);
	}

	@Override
	public Boolean copy(String srcFileName, String destFileName) throws Exception {
		// 复制文件
		Boolean isSuccess = copyFile(srcFileName, destFileName);
		return isSuccess;
	}

	public Boolean move(String srcFileName, String destFileName) throws Exception {
		// 移动文件

		Boolean isSuccess = copy(srcFileName, destFileName);
		if (isSuccess) {
			isSuccess = delete(srcFileName);
		}
		return isSuccess;
	}

	@Override
	public Boolean mkdir(String directory) throws Exception {
		// 建立目录

		Boolean isSuccess = true;

		if (directory.indexOf(".") == -1) {
			isSuccess = createDirEntry(directory, false);
		}
		else {
			isSuccess = false;
			String tip = "文件夹名称不合法";
			System.out.println(tip);
			int n = JOptionPane.showOptionDialog(null, tip, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
		}
		return isSuccess;
	}

	@Override
	public Boolean rmdir(String directory) throws Exception {
		// 删除空目录
		Boolean isSuccess = deleteDirEntry(directory, false, false);
		return isSuccess;
	}

	public Boolean delDir(String direcotry) {
		Boolean isSuccess = deleteDirEntry(direcotry, false, true);
		return isSuccess;
	}

	@Override
	public boolean diskAvailable(int diskIndex) throws Exception {
		return false;
	}

	@Override
	public String printDiskStructur() throws Exception {
		// 打印文件目录结构
		System.out.println("我的磁盘");
		String diskStructureString = print(disk.getRoot(), 0);
		diskStructureString = "我的磁盘\n" + diskStructureString;
		return diskStructureString;
	}

	public void formatDisk() {
		dao.clearDisk();
		for (int i = 3; i < 128; i++) {
			this.getDisk().getFat().clear((byte) i);
		}
		Block[] blocks = this.getDisk().getBlocks();
		for (int i = 2; i < blocks.length; i++) {
			ArrayList<DirEntry> dirEntries = blocks[i].getUsedDirEntry();
			if (dirEntries != null) {
				for (int j = 0; j < dirEntries.size(); j++) {
					DirEntry dirEntry = dirEntries.get(j);
					dirEntry.setProperty((byte) 0, false);
					dirEntry.setName("   ", false);
					dirEntry.setSuffix("  ", false);
				}
			}
		}
		dao.readDisk(DISK_DAT);
	}

	public Boolean change(String destFileName, byte property) {

		Boolean isSuccess = true;

		String[] entrys = destFileName.split("/");// 所有的目录项，包括最后要创建的文件
		String[] name = entrys[entrys.length - 1].split("\\.");// 分割bbb.js

		Vector<Object> vector = getContainFolder(entrys);

		Block block = (Block) vector.get(0);
		Boolean isExist = (Boolean) vector.get(1);

		if (property <= (byte) 4) {
			// 文件
			DirEntry dirEntry = block.getDirEntryByName(name[0], true);

			if (dirEntry == null) {
				String tip = "不存在文件：" + destFileName + "，或者你要更改不可更改的目录属性";
				System.out.println(tip);
				int n = JOptionPane.showOptionDialog(null, tip, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
				isSuccess = false;
			}
			else {
				if (property == dirEntry.getProperty()) {
					String tip = destFileName + "已经是：" + property + "属性";
					System.out.println(tip);
					int n = JOptionPane.showOptionDialog(null, tip, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
					isSuccess = false;
				}
				else {
					switch (property) {
						case (byte) 4:
							dirEntry.setProperty(property, false);
							String tip = "已将文件" + destFileName + "的属性改为普通文件(4)属性";
							System.out.println(tip);
							int n = JOptionPane.showOptionDialog(null, tip, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
							break;
						case (byte) 2:
							dirEntry.setProperty(property, false);
							String tip2 = "已将文件" + destFileName + "的属性改为系统文件(2)属性";
							System.out.println(tip2);
							JOptionPane.showOptionDialog(null, tip2, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
							break;
						case (byte) 1:
							dirEntry.setProperty(property, false);
							String tip3 = "已将文件" + destFileName + "的属性改为只读文件(1)属性";
							System.out.println(tip3);
							JOptionPane.showOptionDialog(null, tip3, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
							break;
						default:
							String tip4 = "不存在属性" + property + "你可以尝试以下属性：(4)普通文件、(2)系统文件、(1)只读文件";
							System.out.println(tip4);
							JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
							isSuccess = false;
					}
				}
			}
		}
		else {
			String tip4 = "试图将文件属性改成目录属性是不允许的，或将目录属性改成目录属性是没必要的";
			System.out.println(tip4);
			JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
			isSuccess = false;
		}
		return isSuccess;
	}

	private boolean isSame(ArrayList<DirEntry> dirEntries, String name, Boolean isFile) {
		Boolean isSame = false;
		for (int i = 0; i < dirEntries.size(); i++) {
			if (isFile) {
				if (name.equals(dirEntries.get(i).getName()) && !dirEntries.get(i).isDir()) {
					isSame = true;
					// String tip4 = "已存在" + dirEntries.get(i).getName() + "目录";
					// System.out.println(tip4);
					// JOptionPane.showConfirmDialog(null, tip4, "消息",
					// JOptionPane.YES_NO_OPTION);
					return isSame;
				}
			}
			else {
				if (name.equals(dirEntries.get(i).getName()) && dirEntries.get(i).isDir()) {
					isSame = true;
					// String tip4 = "已存在" + dirEntries.get(i).getName() + "." +
					// dirEntries.get(i).getSuffix() + "文件";
					// System.out.println(tip4);
					// JOptionPane.showConfirmDialog(null, tip4, "消息",
					// JOptionPane.YES_NO_OPTION);
					return isSame;
				}
			}
		}
		return isSame;
	}

	private Vector<Object> getContainFolder(String[] entrys) {
		Block block = this.disk.getRoot();// 得到根目录,如果是create aaa,则直接在根目录建立aaa文件
		boolean isExist = true;
		for (int i = 0; i < entrys.length - 1; i++) {
			// 找到aaa/bbb.js的bbb.js的目录项所在的盘块
			DirEntry dirEntry = block.getDirEntryByName(entrys[i], false);
			if (dirEntry != null) {
				// 不空，说明可能是文件或文件夹
				if (dirEntry.isDir()) {
					byte beginBlock = block.getDirEntryByName(entrys[i], false).getBeginBlock();
					block = this.disk.getBlockByIndex(beginBlock);// 文件的上一个目录，即包含文件的目录
				}
				else {
					String tip4 = "不存在" + entrys[i] + "目录";
					System.out.println(tip4);
					JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
					isExist = false;
				}

			}
			else {
				String tip4 = "不存在" + entrys[i] + "目录";
				System.out.println(tip4);
				JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
				isExist = false;
			}
		}
		Vector<Object> vector = new Vector<Object>();
		vector.add(block);
		vector.add(isExist);
		return vector;
	}

	private Boolean createDirEntry(String fileName, Boolean isFile) {
		Boolean isSuccess = true;
		// 根据文件（目录）名（路径）返回目录项，此方法不好，应该只负责创建，而不应该在里面验证。
		String[] entrys = fileName.split("/");// 所有的目录项，包括最后要创建的文件
		String[] name = entrys[entrys.length - 1].split("\\.");// 分割bbb.js
		if (name[0].length() < 3) {
			isSuccess = false;
			String tip4 = "命名应占三个字符";
			System.out.println(tip4);
			JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
			return isSuccess;
		}

		// 得到文件/目录项所在目录，如果此过程中有不存在的目录，将isExist设置成false
		Vector<Object> vector = getContainFolder(entrys);
		Block block = (Block) vector.get(0);// 文件所在的目录
		boolean isExist = (Boolean) vector.get(1);// 是否不存在路径中的文件夹

		// 检测是否存在同名文件
		ArrayList<DirEntry> dirEntries = block.getUsedDirEntry();
		boolean isSame = isSame(dirEntries, name[0], isFile);

		if (isExist && !isSame) {
			DirEntry dirEntry = block.allocateEntry();
			Fat.dirEntryForCmd = dirEntry;
			if (dirEntry != null) {
				// dirEntry != null 存在可分配的目录项
				if (name.length == 1) {
					// 如果要新建的目录项没有后缀
					if (name[0].length() <= 3) {
						dirEntry.setName(fixName(name[0], 3), false);
						dirEntry.setSuffix("  ", false);
						if (isFile) {
							dirEntry.setProperty((byte) 4, false);
							String tip4 = "文件：" + fileName + "已创建！";
							System.out.println(tip4);
							// JOptionPane.showOptionDialog(null, tip4, "消息",
							// JOptionPane.DEFAULT_OPTION,
							// JOptionPane.WARNING_MESSAGE, null, null, null);
						}
						else {
							dirEntry.setProperty((byte) 8, false);
							System.out.println();
							String tip4 = "目录：" + fileName + "已创建！";
							System.out.println(tip4);
							// JOptionPane.showOptionDialog(null, tip4, "消息",
							// JOptionPane.DEFAULT_OPTION,
							// JOptionPane.WARNING_MESSAGE, null, null, null);
						}
						dirEntry.setBeginBlock(this.disk.getFat().allocate(isFile), false);
						dirEntry.setLength((byte) 0, false);
					}
					else {
						if (isFile) {
							String tip4 = "根据课程设计要求，文件名的存储空间只有3个字节，不能多于3个字符";
							System.out.println(tip4);
							JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
						}
						else {
							String tip4 = "根据课程设计要求，目录名的存储空间只有3个字节，不能多于3个字符";
							System.out.println(tip4);
							JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
						}
						isSuccess = false;
					}
				}
				else {
					// 如果要新建的目录项有后缀
					if (name[0].length() <= 3 && name[1].length() <= 2) {
						dirEntry.setName(fixName(name[0], 3), false);
						dirEntry.setSuffix(fixName(name[1], 2), false);
						dirEntry.setProperty((byte) 4, false);
						dirEntry.setBeginBlock(this.disk.getFat().allocate(isFile), false);
						dirEntry.setLength((byte) 0, false);
						System.out.println("文件：" + fileName + "已创建！");
					}
					else {
						String tip4 = "根据课程设计要求，文件名的存储空间只有3个字节，不能多于3个字符";
						System.out.println(tip4);
						JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
						isSuccess = false;
					}
				}
			}
			else {
				String tip4 = "文件夹已满8个目录项，根据课程设计要求，" + fileName + "不予分配";
				System.out.println(tip4);
				JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
				isSuccess = false;
			}
		}
		else {
			if (isSame) {
				if (isFile) {
					String withoutSuffix = fileName.substring(0, fileName.lastIndexOf("."));
					String tip4 = "文件：" + withoutSuffix + "已经存在";
					System.out.println(tip4);
					JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
				}
				else {
					String tip4 = "目录：" + fileName + "已经存在";
					System.out.println(tip4);
					JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
				}
				isSuccess = false;
			}
		}
		return isSuccess;
	}

	private Boolean deleteDirEntry(String fileName, Boolean isFile, Boolean isDelDir) {
		// 根据文件（目录）名（路径）删除目录项
		Boolean isSuccess = true;
		String[] entrys = fileName.split("/");// 所有的目录项，包括最后要创建的文件
		String[] name = entrys[entrys.length - 1].split("\\.");// 分割bbb.js

		// 得到文件/目录项所在目录，如果此过程中有不存在的目录，将isExist设置成false
		Vector<Object> vector = getContainFolder(entrys);
		Block block = (Block) vector.get(0);// 文件所在的目录
		boolean isExist = (Boolean) vector.get(1);// 是否不存在路径中的文件夹

		if (isExist) {
			if (isFile) {
				DirEntry dirEntry = block.getDirEntryByName(name[0], isFile);
				if (dirEntry != null) {
					this.disk.getFat().clear(dirEntry.getBeginBlock());
					block.deleteFileEntryByName(name[0]);
					System.out.println("已删除文件：" + fileName);
				}
				else {
					String tip4 = "你删除的：" + fileName + "文件不存在";
					System.out.println(tip4);
					JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
					isSuccess = false;
				}
			}
			else {
				// 删除空目录
				DirEntry dirEntry = block.getDirEntryByName(name[0], isFile);
				if (dirEntry != null) {
					byte beginBlock = dirEntry.getBeginBlock();
					Block block1 = this.disk.getBlockByIndex(beginBlock);
					if (block1.getUsedDirEntry().size() == 0) {
						// 空目录
						this.disk.getFat().clear(dirEntry.getBeginBlock());
						block.deleteEmptyFolderEntryByName(name[0]);
						System.out.println("已删除空目录:" + fileName);
					}
					else {
						if (!isDelDir) {
							String tip4 = "你删除的" + name[0] + "不是空目录，请使用delDir指令";
							System.out.println(tip4);
							JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
							isSuccess = false;
						}
						else {
							ArrayList<DirEntry> dirEntries = block1.getUsedDirEntry();
							for (int i = 0; i < dirEntries.size(); i++) {
								// 递归删除子节点
								DirEntry dirEntry1 = dirEntries.get(i);
								if (dirEntry1.isDir()) {
									deleteDirEntry(fileName + "/" + dirEntry1.getName(), false, true);
								}
								else {
									deleteDirEntry(fileName + "/" + dirEntry1.getName(), true, false);
								}
							}
							this.disk.getFat().clear(dirEntry.getBeginBlock());
							block.deleteEmptyFolderEntryByName(dirEntry.getName());
							System.out.println("已删除非空目录：" + fileName);
						}
					}
				}
				else {
					String tip4 = "你删除的" + name[0] + "文件夹不存在";
					System.out.println(tip4);
					JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
					isSuccess = false;
				}
			}
		}
		return isSuccess;
	}

	public String showFile(String fileName) {
		// 显示文件内容.如aaa/bbb/cc
		String[] entrys = fileName.split("/");// 所有的目录项，包括最后de文件
		// 得到文件/目录项所在目录，如果此过程中有不存在的目录，将isExist设置成false
		Vector<Object> vector = getContainFolder(entrys);
		Block block = (Block) vector.get(0);// 文件所在的目录
		boolean isExist = (Boolean) vector.get(1);// 是否不存在路径中的文件夹
		ArrayList<DirEntry> dirEntries = block.getDirEntries();
		boolean isExistFile = isSame(dirEntries, entrys[entrys.length - 1], true);// 源文件夹下是否存在显示文件
		System.out.println("isExist && isExistFile:" + isExist + isExistFile + ": " + entrys[entrys.length - 1]);
		if (isExist && isExistFile) {
			// ArrayList<DirEntry> dirEntries = block.getUsedDirEntry();
			int i;
			for (i = 0; i < dirEntries.size(); i++) {
				if (entrys[entrys.length - 1].equals(dirEntries.get(i).getName())) {
					DirEntry Entry = block.getDirEntryByName(entrys[entrys.length - 1], true);// 得到文件,这里即cc
					System.out.println(Entry.showAllProperty());
					return "文件名:"+ Entry.getName() +
							", 文件后缀:" + Entry.getSuffix() +
							", 文件类型:" + Entry.getProperty() +
							", 文件起始盘块号:" + Entry.getBeginBlock() +
							", 文件长度:" + Entry.getLength();
				}
			}
		}
		else {
			if (!isExist) {
				String tip4 = "文件路径错误!";
				System.out.println(tip4);
				JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
			}
			else {
				String tip4 = "文件名不存在！";
				System.out.println(tip4);
				JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
			}
		}
		return null;
	}

	public Boolean copyFile(String srcFileName, String destFileName) {
		Boolean isSuccess = true;
		// 复制文件内容：如srcFileName=aaa/bbb/cc.js,destFileName=aaa/ddd/cc.js

		String[] srcFileNames = srcFileName.split("/");// 所有的源目录项，最后一个为文件名
		String[] srcName = srcFileNames[srcFileNames.length - 1].split("\\.");
		Vector<Object> vector = getContainFolder(srcFileNames);
		Block block = (Block) vector.get(0);// 文件所在的目录
		boolean isExist = (Boolean) vector.get(1);// 是否不存在路径中的文件夹
		ArrayList<DirEntry> dirEntries = block.getDirEntries();
		boolean isExistFile = isSame(dirEntries, srcName[0], true);// 源文件夹下是否存在复制文件
		int i;
		if (isExist && isExistFile) {
			if (!destFileName.equals("root")) {
				if (destFileName.length() != 0) {
					destFileName = destFileName + "/" + srcFileNames[srcFileNames.length - 1];
				}
				else {
					destFileName = destFileName + srcFileNames[srcFileNames.length - 1];
				}
			}
			else {
				destFileName = "" + srcFileNames[srcFileNames.length - 1];
			}
			try {
				isSuccess = create(destFileName);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			isSuccess = false;
			String tip4 = "复制失败";
			System.out.println(tip4);
			JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
		}
		return isSuccess;
	}

	public String print(Block root, int depth) {
		String result = "";
		if (root != null) {
			// System.out.println(f.listFiles().length);
			if (!(root.getUsedDirEntry().isEmpty())) {
				ArrayList<DirEntry> fileArray = root.getUsedDirEntry();
				depth++;
				for (int i = 0; i < fileArray.size(); i++) {
					// 递归调用
					System.out.println(getPrint(fileArray.get(i), depth));
					result += getPrint(fileArray.get(i), depth) + "\n";
					byte beginBlock = fileArray.get(i).getBeginBlock();
					result += print(disk.getBlockByIndex(beginBlock), depth);// 递归函数
				}
				// return result;
			}

		}
		return result;
	}

	private String getPrint(DirEntry dirEntry, int depth) {
		// String[] forPrints = f.toString().split("\\\\");
		// String forPrint = forPrints[forPrints.length-1];
		String fileName = dirEntry.getName();
		if (!dirEntry.isDir()) {
			fileName = fileName + "." + dirEntry.getSuffix();
		}
		String s = "";
		for (int i = 0; i < depth; i++) {
			s += "         ";
		}
		fileName = s + "|---" + fileName;
		return fileName;
	}

	private String fixName(String s, int length) {
		// 传进aa 返回 " " + aa，传进a 返回 " " + a
		int space = length - s.length();
		for (int i = 0; i < space; i++) {
			s = " " + s;
		}
		return s;
	}
}
