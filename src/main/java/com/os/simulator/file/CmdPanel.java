package com.os.simulator.file;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Created by Administrator on 2015/11/6.
 */
public class CmdPanel extends JPanel {

	private static final long	serialVersionUID	= -2714079156528904911L;

	private JTextField			jTextField;
	private JLabel				jLabel;
	private FileTreePanel		fileTreePanel;
	private DirContainPanel		dirContainPanel;
	private BlockDisplayPanel	blockDisplayPanel;
	private CMD					cmd;

	public CmdPanel(FileManager fileManager, FileTreePanel fileTreePanel, DirContainPanel dirContainPanel, BlockDisplayPanel blockDisplayPanel) {
		this.fileTreePanel = fileTreePanel;
		this.dirContainPanel = dirContainPanel;
		this.blockDisplayPanel = blockDisplayPanel;
		this.cmd = new CMD(fileManager, fileTreePanel, blockDisplayPanel, dirContainPanel);
		this.jTextField = new JTextField();
		this.jTextField.setPreferredSize(new Dimension(260, 20));
		this.jTextField.setBorder(null);
		this.jTextField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						cmd.input(CmdPanel.this.jTextField.getText());
					}
					catch (Exception e1) {
						System.out.println("cmd错误");
						e1.printStackTrace();
					}
				}
			}
		});
		this.jLabel = new JLabel("请输入命令(可用help命令):");
		this.jLabel.setForeground(Color.white);
		FlowLayout floatLayout = new FlowLayout();
		floatLayout.setAlignment(FlowLayout.LEFT);
		this.setLayout(floatLayout);
		this.add(jLabel);
		this.add(jTextField);
	}

	public JTextField getjTextField() {
		return jTextField;
	}

	public void setjTextField(JTextField jTextField) {
		this.jTextField = jTextField;
	}

	public JLabel getjLabel() {
		return jLabel;
	}

	public void setjLabel(JLabel jLabel) {
		this.jLabel = jLabel;
	}

	public FileTreePanel getFileTreePanel() {
		return fileTreePanel;
	}

	public void setFileTreePanel(FileTreePanel fileTreePanel) {
		this.fileTreePanel = fileTreePanel;
	}

	public DirContainPanel getDirContainPanel() {
		return dirContainPanel;
	}

	public void setDirContainPanel(DirContainPanel dirContainPanel) {
		this.dirContainPanel = dirContainPanel;
	}

	public BlockDisplayPanel getBlockDisplayPanel() {
		return blockDisplayPanel;
	}

	public void setBlockDisplayPanel(BlockDisplayPanel blockDisplayPanel) {
		this.blockDisplayPanel = blockDisplayPanel;
	}

	public CMD getCmd() {
		return cmd;
	}

	public void setCmd(CMD cmd) {
		this.cmd = cmd;
	}
}
