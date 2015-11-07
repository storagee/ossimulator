package com.os.simulator.file;

import static com.os.simulator.file.Constants.FOLDER_PATH;

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
public class DirPanel extends JPanel {

	private Image				fileImg;
	private String				fileName;
	private DirEntry			dirEntry;
	private JPanel				parent;
	private FileManager			fileManager;
	private FileTreePanel		fileTreePanel;
	private BlockDisplayPanel	blockDisplayPanel;

	public DirPanel(FileManager fileManager, final DirEntry dirEntry, final DirContainPanel parent, FileTreePanel fileTreePanel, BlockDisplayPanel blockDisplayPanel) {
		this.parent = parent;
		this.dirEntry = dirEntry;
		this.fileManager = fileManager;
		this.fileTreePanel = fileTreePanel;
		this.blockDisplayPanel = blockDisplayPanel;
		this.fileName = dirEntry.getName();
		this.fileImg = new ImageIcon(FOLDER_PATH).getImage();
		this.setPreferredSize(new Dimension(80, 95));
		this.setBackground(Color.white);

		final PopupMenu popupMenu = new PopupMenu();
		MenuItem menuItem1 = new MenuItem("delete");
		menuItem1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = DirPanel.this.dirEntry.getName();
				int n = JOptionPane.showConfirmDialog(null, "确认要删除" + fileName + "吗？", "请确认", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					try {
						if (Fat.folderPath.length() != 0) {
							DirPanel.this.fileManager.delDir(Fat.folderPath + "/" + fileName);
						}
						else {
							DirPanel.this.fileManager.delDir(Fat.folderPath + fileName);
						}
						DirPanel.this.getParent().remove(DirPanel.this);
						DirPanel.this.parent.updateUI();
						DirEntry dirEntry1 = DirPanel.this.dirEntry;
						if (Fat.folderPath.length() != 0) {
							DirPanel.this.fileTreePanel.deleteChildNode(Fat.folderPath + "/" + dirEntry1.getName() + "." + dirEntry1.getSuffix());
						}
						else {
							DirPanel.this.fileTreePanel.deleteChildNode(Fat.folderPath + dirEntry1.getName() + "." + dirEntry1.getSuffix());
						}
					}
					catch (Exception e1) {
						e1.printStackTrace();
					}
					DirPanel.this.blockDisplayPanel.repaint();
				}
			}
		});
		popupMenu.add(menuItem1);
		this.add(popupMenu);
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				int mods = e.getModifiers();
				if ((mods & InputEvent.BUTTON3_MASK) != 0) {
					popupMenu.show(DirPanel.this, e.getX(), e.getY());
				}
			}
		});

		// 绑定事件
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					DirEntry dirEntry1 = DirPanel.this.dirEntry;
					if (Fat.folderPath.length() == 0) {
						// 长度为零则不加 /
						Fat.folderPath += dirEntry.getName();
					}
					else {
						Fat.folderPath += "/" + dirEntry.getName();
					}
					byte beginBlock = dirEntry1.getBeginBlock();
					Block childBlock = DirPanel.this.fileManager.getDisk().getBlockByIndex(beginBlock);
					parent.repaintFolder(childBlock);
				}
			}
		});
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				Color color = new Color(237, 237, 237);
				DirPanel.this.setBorder(BorderFactory.createLineBorder(color));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				DirPanel.this.setBorder(null);
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
