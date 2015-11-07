package com.os.simulator.file;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Administrator on 2015/10/30.
 */
public class MainFrame extends JFrame {

    private JPanel allFilePanel;//整体的Panel
    private BlockDisplayPanel blockDisplayPanel;
    private FileTreePanel fileTreePanel;
    private JPanel rightPanel;//BorderLayout，右边的Panel
    private DirContainPanel dirContainPanel; //文件夹、文件展示区
    private CmdPanel cmdPanel; //命令输入区
    private FileManager fileManager;

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

    public MainFrame(FileManager fileManager){
        this.fileManager = fileManager;

        //整体的Panel
        this.allFilePanel = new JPanel();
        this.allFilePanel.setSize(639, 390);
        this.allFilePanel.setLayout(new BorderLayout());
        //右边的Panel
        this.rightPanel = new JPanel();
        this.rightPanel.setSize(415, 350);
        this.rightPanel.setLayout(new BorderLayout());

//        this.allFilePanel.setLayout(null);
//        this.allFilePanel.setBounds(0, 0, 683, 384);

        //初始化磁盘展示区域
        byte[] fatContent = fileManager.getDisk().getFat().getFatContent();
        initBlockDisplayPanel(fatContent);

        //初始化文件夹展示区域
        initDirContainPanel();

        //初始化文件树
        Disk disk = fileManager.getDisk();
        initFileTreePanel(disk);

        //初始化文件树后向DirContainPanel中添加文件树，用于更新
        dirContainPanel.setFileTreePanel(this.fileTreePanel);
        dirContainPanel.showRooBlock();

        //初始化CMD
        initCmdPanel();

        this.add(allFilePanel);
    }

    private void initBlockDisplayPanel(byte[] fatContent){
        this.blockDisplayPanel = new BlockDisplayPanel(fatContent);
        this.blockDisplayPanel.setSize(500, 500);
        this.blockDisplayPanel.setPreferredSize(new Dimension(415, 152));
        this.rightPanel.add(blockDisplayPanel, BorderLayout.SOUTH);
        this.allFilePanel.add(this.rightPanel, BorderLayout.CENTER);
    }

    private void initFileTreePanel(Disk disk){
        this.fileTreePanel = new FileTreePanel(disk, this.dirContainPanel);
        this.fileTreePanel.setPreferredSize(new Dimension(200, 364));
        this.allFilePanel.add(fileTreePanel, BorderLayout.WEST);
    }

    private void initDirContainPanel(){
        //文件夹区域
        this.dirContainPanel = new DirContainPanel(this.fileManager, blockDisplayPanel);
        this.dirContainPanel.setBackground(Color.white);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        this.dirContainPanel.setLayout(flowLayout);
        this.rightPanel.add(dirContainPanel, BorderLayout.CENTER);
    }

    private void initCmdPanel(){
        this.cmdPanel = new CmdPanel(fileManager, this.fileTreePanel, this.dirContainPanel, this.blockDisplayPanel);
        this.cmdPanel.setBackground(Color.gray);
        this.cmdPanel.setPreferredSize(new Dimension(420, 30));
        this.rightPanel.add(cmdPanel, BorderLayout.NORTH);
    }
}
