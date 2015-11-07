package com.os.simulator.file;

import javax.swing.WindowConstants;

/**
 * Created by Administrator on 2015/10/27.
 */
public class Main {

	public static void main(String[] args) {
		setLookAndFeel();
		FileManager fileManager = new FileManager();
		try {
			// System.out.println("*************");
			// fileManager.mkdir("aaa");
			// fileManager.create("aaa.js");
			//// fileManager.change("aaa", (byte) 8);
			// fileManager.mkdir("aaa/bbb");
			// fileManager.mkdir("aaa/bbb/ccc");
			// fileManager.mkdir("aaa/bbb/ddd");
			// fileManager.mkdir("aaa/bbb/eee");
			// fileManager.mkdir("aaa/bbb/fff");
			// fileManager.mkdir("aaa/bbb/ggg");
			// fileManager.mkdir("aaa/bbb/ggg/hhh");
			// fileManager.mkdir("aaa/bbb/ggg/iii");
			// fileManager.mkdir("aaa/bbb/ggg/jjj");
			// fileManager.mkdir("aaa/bbb/ggg/kkk");
			// fileManager.create("ccc.js");
			// fileManager.create("cxc.js");
			// fileManager.create("fcc.js");
			// fileManager.create("scc.js");
			// fileManager.create("kcc.js");
			//// fileManager.formatDisk();
			// fileManager.create("aaa/bbb/ggg/aaa.js");
			// fileManager.create("aaa/bbb/ggg/bbb.js");
			// fileManager.create("aaa/bbb/ggg/ccc.js");
			// fileManager.create("aaa/bbb/ggg/ddd.js");
			// fileManager.create("aaa/bbb/ggg/eee.js");
			//// fileManager.copy("aaa/bbb/ggg/ccc.js", "aaa/www");
			// fileManager.printDiskStructur();
			// fileManager.formatDisk();
			// fileManager.delDir("aaa");
			// fileManager.create("aaa/aaa.js");
			// fileManager.formatDisk();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// fileManager.formatDisk();

		//
		// JFrame.setDefaultLookAndFeelDecorated(true); //加上此语句连同窗体外框也改变
		// JDialog.setDefaultLookAndFeelDecorated(true); //加上此语句会使弹出的对话框也改变
		// try {
		//
		//// SubstanceImageWatermark watermark = new
		// SubstanceImageWatermark(FSSui.class.getResourceAsStream("/img/4.jpg"));
		//// watermark.setKind(SubstanceConstants.ImageWatermarkKind.SCREEN_CENTER_SCALE);
		//// watermark.setOpacity((float) 1.0); //更改水印透明度
		// UIManager.setLookAndFeel(new
		// SubstanceRavenGraphiteGlassLookAndFeel());
		//// SubstanceSkin skin = new CremeSkin().withWatermark(watermark);
		//
		// //此语句设置外观
		// SubstanceLookAndFeel.setSkin(skin);
		//
		// } catch (UnsupportedLookAndFeelException ex) {
		//// Logger.getLogger(FSSui.class.getName()).log(Level.SEVERE, null,
		// ex);
		// }
		try {
			// fileManager.delete("aaa/222.22");
			// fileManager.delete("aaa/nnn.nn");
			// fileManager.delete("aaa/xxx.xx");
			// fileManager.delete("aaa/zzz.zz");
			// fileManager.delete("aaa/rrr.rr");
			// fileManager.formatDisk();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		MainFrame mainFrame = new MainFrame(fileManager);
		mainFrame.setTitle("文件管理");
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setSize(639, 397);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setLayout(null);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}

	public static void setLookAndFeel() {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

	}
}
