package com.os.simulator.file;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/4.
 */
public class DirContainPanel extends JPanel {

    private FileManager fileManager;
    private MouseListener mouseListener;
    private String folderPath;
    private BlockDisplayPanel blockDisplayPanel;
    private FileTreePanel fileTreePanel;

    public DirContainPanel(FileManager fileManager, BlockDisplayPanel blockDisplayPanel) {
        this.blockDisplayPanel = blockDisplayPanel;
        this.fileManager = fileManager;
    }

    public void showRooBlock(){
        Block root = this.fileManager.getDisk().getRoot();
        showFileAndFolder(root);
    }

    public void repaintFolder(Block block) {
        showFileAndFolder(block);
//        this.repaint();
    }

    private void showFileAndFolder(final Block block) {
        this.removeAll();
        this.removeMouseListener(this.mouseListener);
        ArrayList<DirEntry> dirEntries = block.getUsedDirEntry();
        for (int i = 0; i < dirEntries.size(); i++) {
            DirEntry dirEntry = dirEntries.get(i);
            if (!dirEntry.isDir()) {
                FilePanel filePanel = new FilePanel(this.fileManager, dirEntry, blockDisplayPanel, DirContainPanel.this, fileTreePanel);
                this.add(filePanel);
            } else {
                DirPanel dirPanel = new DirPanel(this.fileManager, dirEntry, this, fileTreePanel, blockDisplayPanel);
                this.add(dirPanel);
            }
        }
        this.updateUI();
        final PopupMenu popupMenu = new PopupMenu();
        MenuItem menuItem1 = new MenuItem("new File");
        MenuItem menuItem2 = new MenuItem("new Folder");
        MenuItem menuItem3 = new MenuItem("paste");
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //创建文件
                String inputValue = JOptionPane.showInputDialog("Please input file name");
                if (inputValue != null){
                    String[] inputs = inputValue.split("\\.");
                    try {
                        DirEntry dirEntry = block.getDirEntryByName(inputs[0], true);
                        if (dirEntry == null) {
                            //文件不存在才创建
                            String path = "";
                            if (Fat.folderPath.length() == 0) {
                                path = Fat.folderPath + inputValue;
                            } else {
                                path = Fat.folderPath + "/" + inputValue;
                            }
                            Boolean isSuccess = fileManager.create(path);
                            if(isSuccess){
                                DirEntry dirEntry1 = block.getDirEntryByName(inputs[0], true);//创建之后的dirEntry1
                                DirContainPanel.this.add(new FilePanel(fileManager, dirEntry1, blockDisplayPanel, DirContainPanel.this, fileTreePanel));
                                DirContainPanel.this.updateUI();
                                DirContainPanel.this.blockDisplayPanel.repaint();
                                DirContainPanel.this.fileTreePanel.addChildNode(Fat.folderPath, dirEntry1);
                            }
                        }else{
                            String tip4 = "已存在" + dirEntry.getName() + "文件";
                            System.out.println(tip4);
                            JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //创建文件夹
                String inputValue = JOptionPane.showInputDialog("Please input folder name");
                if(inputValue != null){
                    try {
                        DirEntry dirEntry = block.getDirEntryByName(inputValue, false);
                        if (dirEntry == null) {
                            //文件夹不存在才创建
                            Boolean isSuccess;

                            if(Fat.folderPath.length() != 0){
                                isSuccess = fileManager.mkdir(Fat.folderPath + "/" + inputValue);
                            }else{
                                isSuccess = fileManager.mkdir(Fat.folderPath + inputValue);
                            }
                            if(isSuccess){
                                DirEntry dirEntry1 = block.getDirEntryByName(inputValue, false);//创建之后的dirEntry1
                                DirContainPanel.this.add(new DirPanel(fileManager, dirEntry1, DirContainPanel.this, fileTreePanel, blockDisplayPanel));
                                DirContainPanel.this.updateUI();
                                DirContainPanel.this.blockDisplayPanel.repaint();
                                DirContainPanel.this.fileTreePanel.addChildNode(Fat.folderPath, dirEntry1);
                            }
                        }else{
                            String tip4 = "已存在" + dirEntry.getName() + "目录";
                            System.out.println(tip4);
                            JOptionPane.showOptionDialog(null, tip4, "消息", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        menuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Fat.isCopy != null){
                    if (Fat.isCopy) {
                        try {
                            Boolean isSuccess = fileManager.copy(Fat.copyPath, Fat.folderPath);
                            if (isSuccess) {
                                String fileName = Fat.copyPath.substring(Fat.copyPath.lastIndexOf("/") + 1, Fat.copyPath.length());
                                String[] fileNames = fileName.split("\\.");
                                System.out.println("fileName:" + fileName);
                                DirEntry dirEntry1 = block.getDirEntryByName(fileNames[0], true);//创建之后的dirEntry1
                                DirContainPanel.this.add(new FilePanel(fileManager, dirEntry1, blockDisplayPanel, DirContainPanel.this, fileTreePanel));
                                DirContainPanel.this.updateUI();
                                DirContainPanel.this.blockDisplayPanel.repaint();
                                DirContainPanel.this.fileTreePanel.addChildNode(Fat.folderPath, dirEntry1);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        try {
                            Boolean isSuccess = fileManager.move(Fat.copyPath, Fat.folderPath);
                            if (isSuccess) {
                                String fileName = Fat.copyPath.substring(Fat.copyPath.lastIndexOf("/") + 1, Fat.copyPath.length());
                                String[] fileNames = fileName.split("\\.");
                                System.out.println("fileName:" + fileName);
                                DirEntry dirEntry1 = block.getDirEntryByName(fileNames[0], true);//创建之后的dirEntry1
                                DirContainPanel.this.add(new FilePanel(fileManager, dirEntry1, blockDisplayPanel, DirContainPanel.this, fileTreePanel));
                                DirContainPanel.this.updateUI();
                                DirContainPanel.this.blockDisplayPanel.repaint();
                                DirContainPanel.this.fileTreePanel.deleteChildNode(Fat.copyPath);
                                DirContainPanel.this.fileTreePanel.addChildNode(Fat.folderPath, dirEntry1);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);
        this.add(popupMenu);
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mods = e.getModifiers();
                if ((mods & InputEvent.BUTTON3_MASK) != 0) {
                    popupMenu.show(DirContainPanel.this, e.getX(), e.getY());
                }
            }
        };
        this.mouseListener = mouseListener;
        this.addMouseListener(mouseListener);
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public BlockDisplayPanel getBlockDisplayPanel() {
        return blockDisplayPanel;
    }

    public void setBlockDisplayPanel(BlockDisplayPanel blockDisplayPanel) {
        this.blockDisplayPanel = blockDisplayPanel;
    }

    public FileTreePanel getFileTreePanel() {
        return fileTreePanel;
    }

    public void setFileTreePanel(FileTreePanel fileTreePanel) {
        this.fileTreePanel = fileTreePanel;
    }
}
