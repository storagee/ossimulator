//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.os.simulator.file;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

public class CMD {

	FileManager			fileManager;
	FileTreePanel		fileTreePanel;
	BlockDisplayPanel	blockDisplayPanel;
	DirContainPanel		dirContainPanel;

	public CMD(FileManager fileManager, FileTreePanel fileTreePanel, BlockDisplayPanel blockDisplayPanel, DirContainPanel dirContainPanel) {
		this.fileManager = fileManager;
		this.fileTreePanel = fileTreePanel;
		this.blockDisplayPanel = blockDisplayPanel;
		this.dirContainPanel = dirContainPanel;
	}

	public void input(String commend) throws Exception {
		String[] commends = commend.split(" ");
		if (commends[0].equals("create")) {
			if (commends.length == 2) {
				Boolean isSuccess = this.fileManager.create(commends[1]);
				if (isSuccess.booleanValue()) {
					String folderPath;
					if (commends[1].lastIndexOf("/") != -1) {
						String filePath = commends[1];
						folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
					}
					else {
						folderPath = "";
					}

					DefaultMutableTreeNode node1 = this.fileTreePanel.addChildNode(folderPath, Fat.dirEntryForCmd);
					if (Fat.folderPath.equals(folderPath)) {
						if (node1.getUserObject() instanceof String) {
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getRoot());
						}
						else {
							DirEntry parentDirEntry = (DirEntry) node1.getUserObject();
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getBlockByIndex(parentDirEntry.getBeginBlock()));
						}
					}

					this.blockDisplayPanel.repaint();
				}
			}
			else {
				errorTip();
			}
		}
		else if (commends[0].equals("delete")) {
			if (commends.length == 2) {
				this.fileManager.delete(commends[1]);
				Boolean isSuccess = this.fileManager.delete(commends[1]);
				if (isSuccess) {
					String folderPath;
					if (commends[1].lastIndexOf("/") != -1) {
						String filePath = commends[1];
						folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
					}
					else {
						folderPath = "";
					}
					DefaultMutableTreeNode parentNode = this.fileTreePanel.deleteChildNode(commends[1]);
					if (Fat.folderPath.equals(folderPath)) {
						if (parentNode.getUserObject() instanceof String) {
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getRoot());
						}
						else {
							DirEntry parentDirEntry = (DirEntry) parentNode.getUserObject();
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getBlockByIndex(parentDirEntry.getBeginBlock()));
						}
					}
					this.blockDisplayPanel.repaint();
				}
			}
			else {
				errorTip();
			}
		}
		else if (commends[0].equals("show")) {
			if (commends.length == 2) {
				String filePathExceptSuffix;
				if (commends[1].lastIndexOf(".") != -1) {
					filePathExceptSuffix = commends[1].substring(0, commends[1].lastIndexOf("."));
				}
				else {
					filePathExceptSuffix = commends[1];
				}
				String massage = this.fileManager.show(filePathExceptSuffix);
				if (massage != null) {
					JOptionPane.showOptionDialog(null, massage, "属性", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
				}
			}
			else {
				errorTip();
			}
		}
		else if (commends[0].equals("copy")) {
			if (commends.length == 3) {
				Boolean isSuccess = this.fileManager.copy(commends[1], commends[2]);
				if (isSuccess) {
					String folderPath;
					if (commends[2].equals("root")) {
						folderPath = "";
					}
					else {
						folderPath = commends[2];
					}
					DefaultMutableTreeNode node1 = this.fileTreePanel.addChildNode(folderPath, Fat.dirEntryForCmd);
					if (Fat.folderPath.equals(folderPath)) {
						if (node1.getUserObject() instanceof String) {
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getRoot());
						}
						else {
							DirEntry parentDirEntry = (DirEntry) node1.getUserObject();
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getBlockByIndex(parentDirEntry.getBeginBlock()));
						}
					}

					this.blockDisplayPanel.repaint();
				}

			}
			else {
				errorTip();
			}
		}
		else if (commends[0].equals("move")) {
			if (commends.length == 3) {
				Boolean isSuccess = this.fileManager.move(commends[1], commends[2]);
				if (isSuccess) {
					String destPath;
					if (commends[2].equals("root")) {
						destPath = "";
					}
					else {
						destPath = commends[2];
					}
					String srcPath;
					if (commends[1].lastIndexOf("/") != -1) {
						srcPath = commends[1].substring(0, commends[1].lastIndexOf("/"));
					}
					else {
						srcPath = "";
					}
					DefaultMutableTreeNode node1 = this.fileTreePanel.addChildNode(destPath, Fat.dirEntryForCmd);
					DefaultMutableTreeNode parentNode = this.fileTreePanel.deleteChildNode(commends[1]);
					if (Fat.folderPath.equals(destPath)) {
						// 所在的目录是目的目录，则刷新
						if (node1.getUserObject() instanceof String) {
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getRoot());
						}
						else {
							DirEntry parentDirEntry = (DirEntry) node1.getUserObject();
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getBlockByIndex(parentDirEntry.getBeginBlock()));
						}
					}
					else if (Fat.folderPath.equals(srcPath)) {
						// 所在的目录是源目录，则刷新
						if (parentNode.getUserObject() instanceof String) {
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getRoot());
						}
						else {
							DirEntry parentDirEntry = (DirEntry) parentNode.getUserObject();
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getBlockByIndex(parentDirEntry.getBeginBlock()));
						}
					}

					this.blockDisplayPanel.repaint();
				}
			}
			else {
				errorTip();
			}
		}
		else if (commends[0].equals("mkdir")) {
			if (commends.length == 2) {
				Boolean isSuccess = this.fileManager.mkdir(commends[1]);
				if (isSuccess) {
					String folderPath;
					if (commends[1].lastIndexOf("/") != -1) {
						String filePath = commends[1];
						folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
					}
					else {
						folderPath = "";
					}
					DefaultMutableTreeNode node1 = this.fileTreePanel.addChildNode(folderPath, Fat.dirEntryForCmd);
					if (Fat.folderPath.equals(folderPath)) {
						if (node1.getUserObject() instanceof String) {
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getRoot());
						}
						else {
							DirEntry parentDirEntry = (DirEntry) node1.getUserObject();
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getBlockByIndex(parentDirEntry.getBeginBlock()));
						}
					}

					this.blockDisplayPanel.repaint();
				}
			}
			else {
				errorTip();
			}
		}
		else if (commends[0].equals("rmdir")) {
			if (commends.length == 2) {
				Boolean isSuccess = this.fileManager.rmdir(commends[1]);
				if (isSuccess) {
					String folderPath;
					if (commends[1].lastIndexOf("/") != -1) {
						String filePath = commends[1];
						folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
					}
					else {
						folderPath = "";
					}
					DefaultMutableTreeNode parentNode = this.fileTreePanel.deleteChildNode(commends[1]);
					if (Fat.folderPath.equals(folderPath)) {
						if (parentNode.getUserObject() instanceof String) {
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getRoot());
						}
						else {
							DirEntry parentDirEntry = (DirEntry) parentNode.getUserObject();
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getBlockByIndex(parentDirEntry.getBeginBlock()));
						}
					}
					this.blockDisplayPanel.repaint();
				}
			}
			else {
				errorTip();
			}
		}
		else if (commends[0].equals("deldir")) {
			if (commends.length == 2) {
				Boolean isSuccess = this.fileManager.delDir(commends[1]);
				if (isSuccess) {
					String folderPath;
					if (commends[1].lastIndexOf("/") != -1) {
						String filePath = commends[1];
						folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
					}
					else {
						folderPath = "";
					}
					DefaultMutableTreeNode parentNode = this.fileTreePanel.deleteChildNode(commends[1]);
					if (Fat.folderPath.equals(folderPath)) {
						if (parentNode.getUserObject() instanceof String) {
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getRoot());
						}
						else {
							DirEntry parentDirEntry = (DirEntry) parentNode.getUserObject();
							this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getBlockByIndex(parentDirEntry.getBeginBlock()));
						}
					}
					this.blockDisplayPanel.repaint();
				}
			}
			else {
				errorTip();
			}
		}
		else if (commends[0].equals("change")) {
			if (commends.length == 3) {
				this.fileManager.change(commends[1], (byte) Integer.parseInt(commends[2]));
			}
			else {
				errorTip();
			}
		}
		else if (commends[0].equals("format")) {
			if (commends.length == 1) {
				int n = JOptionPane.showConfirmDialog(null, "确认格式化磁盘吗？", "请确认", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					this.fileManager.formatDisk();
					this.blockDisplayPanel.repaint();
					this.dirContainPanel.repaintFolder(this.fileManager.getDisk().getRoot());
					this.fileTreePanel.clear();
				}
			}
			else {
				errorTip();
			}
		}
		else if (commends[0].equals("print")) {
			String structure = this.fileManager.printDiskStructur();
			JOptionPane.showOptionDialog(null, structure, "目录结构", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		}
		else if (commends[0].equals("help")) {
			String helpMessage = "指令格式如下:\n"+
									"1.create fil.js 或 create dir/fil.js\n" +
									"    在根目录（我的磁盘）创建fil.js  或 在根目录下的dir目录创建fil.js\n\n" +
									"2.delete fil.js 或 delete dir/fil.js 删除文件\n\n" +
									"3.show fil.js 或 show dir/.../fil.js 显示文件属性\n\n" +
									"4.copy org.js dir    即将org.js复制到dir目录下\n\n" +
									"5.mkdir dir  创建dir目录\n\n" +
									"6.rmdir dir  删除dir目录  dir为空\n\n" +
									"7.deldir dir 删除dir目录  dir可以不空\n\n" +
									"8.move fil.js dir/dir2  将fil.js文件移动到dir/dir2目录下\n\n" +
									"9.change dir/dir2/fil.js 1 将dir/dir2目录下的fil.js属性改为只读文件:1\n" +
									"    普通文件:4, 系统文件:2\n\n" +
									"10.format  格式化磁盘\n\n" +
									"11.print  打印目录结构\n\n";
			JOptionPane.showOptionDialog(null, helpMessage, "目录结构", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		}
		else {
			errorTip();
		}

	}

	private void errorTip() {
		String tip4 = "输入命令格式错误";
		System.out.println(tip4);
		JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
	}
}
