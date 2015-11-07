package com.os.simulator.file;

import static com.os.simulator.file.Constants.FILE_PATH;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Created by Administrator on 2015/11/4.
 */
public class FilePanel extends JPanel {

	private Image				fileImg;
	private String				fileName;
	private DirEntry			dirEntry;
	private FileManager			fileManager;
	private BlockDisplayPanel	blockDisplayPanel;
	private FileTreePanel		fileTreePanel;

	public BlockDisplayPanel getBlockDisplayPanel() {
		return blockDisplayPanel;
	}

	public void setBlockDisplayPanel(BlockDisplayPanel blockDisplayPanel) {
		this.blockDisplayPanel = blockDisplayPanel;
	}

	public FilePanel(	final FileManager fileManager, final DirEntry dirEntry, final BlockDisplayPanel blockDisplayPanel, final DirContainPanel dirContainPanel,
						FileTreePanel fileTreePanel) {
		this.fileManager = fileManager;
		this.dirEntry = dirEntry;
		this.fileName = dirEntry.getName() + "." + dirEntry.getSuffix();
		this.fileImg = new ImageIcon(FILE_PATH).getImage();
		this.fileTreePanel = fileTreePanel;
		this.setPreferredSize(new Dimension(80, 95));
		this.setBackground(Color.white);

		final PopupMenu popupMenu = new PopupMenu();
		MenuItem menuItem1 = new MenuItem("copy");
		MenuItem menuItem2 = new MenuItem("cut");
		MenuItem menuItem3 = new MenuItem("delete");
		MenuItem menuItem4 = new MenuItem("property");
		menuItem1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DirEntry dirEntry1 = FilePanel.this.dirEntry;
				Fat.isCopy = true;
				if (Fat.folderPath.length() != 0) {
					Fat.copyPath = Fat.folderPath + "/" + dirEntry1.getName() + "." + dirEntry1.getSuffix();
				}
				else {
					Fat.copyPath = Fat.folderPath + dirEntry1.getName() + "." + dirEntry1.getSuffix();
				}
			}
		});
		menuItem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DirEntry dirEntry1 = FilePanel.this.dirEntry;
				Fat.isCopy = false;
				if (Fat.folderPath.length() != 0) {
					Fat.copyPath = Fat.folderPath + "/" + dirEntry1.getName() + "." + dirEntry1.getSuffix();
				}
				else {
					Fat.copyPath = Fat.folderPath + dirEntry1.getName() + "." + dirEntry1.getSuffix();
				}
			}
		});
		menuItem3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = FilePanel.this.dirEntry.getName() + "." + FilePanel.this.dirEntry.getSuffix();
				int n = JOptionPane.showConfirmDialog(null, "确认要删除" + fileName + "吗？", "请确认", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					try {
						if (Fat.folderPath.length() != 0) {
							fileManager.delete(Fat.folderPath + "/" + fileName);
						}
						else {
							fileManager.delete(Fat.folderPath + fileName);
						}
						FilePanel.this.getParent().remove(FilePanel.this);
						dirContainPanel.updateUI();
						DirEntry dirEntry1 = FilePanel.this.dirEntry;
						if (Fat.folderPath.length() != 0) {
							FilePanel.this.fileTreePanel.deleteChildNode(Fat.folderPath + "/" + dirEntry1.getName() + "." + dirEntry1.getSuffix());
						}
						else {
							FilePanel.this.fileTreePanel.deleteChildNode(Fat.folderPath + dirEntry1.getName() + "." + dirEntry1.getSuffix());
						}
					}
					catch (Exception e1) {
						e1.printStackTrace();
					}
					blockDisplayPanel.repaint();
				}
			}
		});

		menuItem4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DirEntry dirEntry1 = FilePanel.this.dirEntry;
				String fileProperty = "文件名:"+ dirEntry1.getName() +
										", 文件后缀:" + dirEntry1.getSuffix() +
										", 文件类型:" + dirEntry1.getProperty() +
										", 文件起始盘块号:" + dirEntry1.getBeginBlock() +
										", 文件长度:" + dirEntry1.getLength();
				JOptionPane.showMessageDialog(null, fileProperty, "文件属性", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		popupMenu.add(menuItem1);
		popupMenu.add(menuItem2);
		popupMenu.add(menuItem3);
		popupMenu.add(menuItem4);
		this.add(popupMenu);
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				int mods = e.getModifiers();
				if ((mods & InputEvent.BUTTON3_MASK) != 0) {
					popupMenu.show(FilePanel.this, e.getX(), e.getY());
				}
			}
		});

		// 绑定事件
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				Color color = new Color(237, 237, 237);
				FilePanel.this.setBorder(BorderFactory.createLineBorder(color));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				FilePanel.this.setBorder(null);
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(fileImg, 0, 0, 80, 80, this);
		g.drawString(this.fileName, 25, 90);
	}
}
