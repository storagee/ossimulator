package com.os.simulator.file;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by Administrator on 2015/11/2.
 */
public class FileTreePanel extends JPanel {
    private JTree tree;
    private Disk disk;
    private JScrollPane jScrollPane;
    private DefaultMutableTreeNode rootNode;
    private MouseAdapter mouseAdapter;

    public FileTreePanel(Disk disk, final DirContainPanel dirContainPanel) {
        this.disk = disk;
        Block rootBlock = disk.getRoot();
        this.setLayout(new BorderLayout());
        rootNode = new DefaultMutableTreeNode("我的磁盘");
        tree = new JTree(rootNode);
        addChildNodes(rootBlock, rootNode);
        this.mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
                if (treePath != null) {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                    Object object = treeNode.getUserObject();
                    if (object instanceof DirEntry) {
                        DirEntry dirEntry = (DirEntry) object;
                        if (dirEntry.isDir()) {
                            String treePathString = tree.getLeadSelectionPath().toString();
                            String[] treePaths = treePathString.split(",");
                            Fat.folderPath = "";
                            for (int i = 1; i < treePaths.length; i++) {
                                if (i == 1) {
                                    if (treePaths.length == 2) {
                                        Fat.folderPath += treePaths[i].substring(0, treePaths[i].length() - 1).trim();
                                    } else {
                                        Fat.folderPath += treePaths[i].trim();
                                    }
                                } else if (i < treePaths.length - 1) {
                                    Fat.folderPath = Fat.folderPath + "/" + treePaths[i].trim();
                                } else {
                                    Fat.folderPath = Fat.folderPath + "/" + treePaths[i].substring(0, treePaths[i].length() - 1).trim();
                                }
                            }
                            Block block = FileTreePanel.this.disk.getBlockByIndex(dirEntry.getBeginBlock());
                            dirContainPanel.repaintFolder(block);
                        }
                    }else{
                        Fat.folderPath = "";
                        dirContainPanel.repaintFolder(FileTreePanel.this.disk.getRoot());
                    }
                }
            }
        };
        tree.addMouseListener(mouseAdapter);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(true);
        JScrollPane scrollPane = new JScrollPane(tree);
        this.add(scrollPane);
    }

    public void addChildNodes(Block block, DefaultMutableTreeNode node) {
        if (block != null) {
//            System.out.println(f.listFiles().length);
            if (!(block.getUsedDirEntry().isEmpty())) {
                ArrayList<DirEntry> fileArray = block.getUsedDirEntry();
                for (int i = 0; i < fileArray.size(); i++) {
                    //递归调用
                    DirEntry dirEntry = fileArray.get(i);
                    DefaultMutableTreeNode childNode = null;
                    if (!dirEntry.isDir()) {
                        //文件
                        childNode = new DefaultMutableTreeNode(dirEntry);
                    } else {
                        childNode = new DefaultMutableTreeNode(dirEntry);
                    }
                    node.add(childNode);
                    byte beginBlock = (byte) dirEntry.getBeginBlock();
//                        System.out.println(beginBlock);
                    addChildNodes(disk.getBlockByIndex(beginBlock), childNode);//递归函数
                }
            }
        }
    }

    public DefaultMutableTreeNode addChildNode(String path, DirEntry dirEntry){
        //增加一个节点，并返回所增加节点的父节点的DirEntry
        String[] paths = path.split("/");
        int i = 0;
        int length = paths.length;
        Enumeration enumeration = this.rootNode.children();//声明节点枚举对象
        DefaultMutableTreeNode node = enumerate(paths, i, length, enumeration);
        if(node == null){
            node = this.rootNode;
        }
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(dirEntry);
        node.add(childNode);
        this.tree.removeMouseListener(this.mouseAdapter);
        this.tree.addMouseListener(this.mouseAdapter);
        this.updateUI();
        this.tree.updateUI();
        return node;
    }

    public DefaultMutableTreeNode deleteChildNode(String path){
        String pathAdjust;
        if(path.lastIndexOf(".") != -1){
            pathAdjust = path.substring(0, path.lastIndexOf("."));
        }else{
            pathAdjust = path;
        }
        String[] paths = pathAdjust.split("/");
        int i = 0;
        int length = paths.length;
        Enumeration enumeration = this.rootNode.children();//声明节点枚举对象
        DefaultMutableTreeNode node = enumerate(paths, i, length, enumeration);
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
        node.removeFromParent();
//        DefaultTreeModel model = (DefaultTreeModel)this.tree.getModel();
//        model.removeNodeFromParent(node);
        this.tree.removeMouseListener(this.mouseAdapter);
        this.tree.addMouseListener(this.mouseAdapter);
        this.updateUI();
        this.tree.updateUI();
        return parent;
    }

    public DefaultMutableTreeNode enumerate(String[] paths, int i,int length, Enumeration enumeration){
        DefaultMutableTreeNode theNode = null;
        while (enumeration.hasMoreElements()){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)enumeration.nextElement();
            Object object = node.getUserObject();
            DirEntry dirEntry;
            if(object instanceof DirEntry){
                dirEntry  = (DirEntry)object;
                if(dirEntry.getName().equals(paths[i])){
                    if(i < paths.length -1){
                        i++;
                        enumeration = node.children();
                        node = enumerate(paths, i, length, enumeration);
                        return node;
                    }else{
                        theNode = node;
                        return theNode;
                    }
                }
            }
        }
        return theNode;
    }

    public void clear(){
        this.rootNode.removeAllChildren();
        this.updateUI();
        this.tree.updateUI();
    }
}
