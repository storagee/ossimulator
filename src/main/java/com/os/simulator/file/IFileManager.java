package com.os.simulator.file;


import java.io.Serializable;

public interface IFileManager extends Serializable{

	/**
	 * 创建文件
	 *
	 * @param fileName 文件名
	 *
	 * @throws Exception 当操作失败时抛出异常
	 */
	Boolean create(String fileName) throws Exception;

	/**
	 * @param fileName
	 *
	 * @throws Exception 当操作失败时抛出异常
	 */
	Boolean delete(String fileName) throws Exception;

	/**
	 * @param fileName
	 *
	 * @return 返回文件的内容
	 *
	 * @throws Exception 当操作失败时抛出异常
	 */
	String show(String fileName) throws Exception;

	/**
	 * @param srcFileName  源文件名
	 * @param destFileName 目标文件名
	 *
	 * @throws Exception 当操作失败时抛出异常
	 */
	Boolean copy(String srcFileName, String destFileName) throws Exception;

	/**
	 * @param directory 要创建的文件夹名字
	 *
	 * @throws Exception 当操作失败时抛出异常
	 */
	Boolean mkdir(String directory) throws Exception;

	/**
	 * @param directory 要删除的文件夹的名字
	 *
	 * @throws Exception 当操作失败时抛出异常
	 */
	Boolean rmdir(String directory) throws Exception;

	/**
	 * 返回磁盘是否空闲还是占用
	 *
	 * @param diskIndex 磁盘号
	 *
	 * @return true 如果空闲,否则 false
	 *
	 * @throws Exception 当操作失败时抛出异常
	 */
	boolean diskAvailable(int diskIndex) throws Exception;

	//@formatter:off
	/**
	 *
	 * 打印磁盘目录结构
	 * @return 返回磁盘目录结构的字符串表示形式, 一个示例的返回字符串:
	 *
	 * |---root
	 * 		|---aaa
	 * 			|---bbb.js
	 * 			|---ccc.js
	 * 		|---bbb
	 * 		|---ccc
	 * @throws Exception 当操作失败时抛出异常
	 */
	 // @formatter:on
	String printDiskStructur() throws Exception;
}
