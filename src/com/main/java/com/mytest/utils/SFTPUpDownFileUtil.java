package com.mytest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP 上传、下载文件
 * 
 * @author da.zhang
 */
public class SFTPUpDownFileUtil {
	
	/**
	 * 连接sftp服务器
	 * @param host 主机
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 * @throws JSchException 
	 */
	public static ChannelSftp connect(String host, int port, String username, String password) throws JSchException {
		JSch jsch = new JSch();
		jsch.getSession(username, host, port);
		Session sshSession = jsch.getSession(username, host, port);
		sshSession.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		sshSession.setConfig(sshConfig);
		sshSession.connect();
		Channel channel = sshSession.openChannel("sftp");
		channel.connect();
		ChannelSftp sftp = (ChannelSftp) channel;
		return sftp;
	}

	/**
	 * 上传文件
	 * @param directory 上传的目录
	 * @param uploadFile 要上传的文件
	 * @param sftp
	 * @throws SftpException 
	 * @throws FileNotFoundException 
	 */
	public static void upload(String directory, String uploadFile, ChannelSftp sftp) throws SftpException, FileNotFoundException {
		sftp.cd(directory);
		File file=new File(uploadFile);
		sftp.put(new FileInputStream(file), file.getName());
	}

	/**
	 * 下载文件
	 * @param directory 下载目录
	 * @param downloadFile 下载的文件
     * @param saveFilePath 存在本地的路径
	 * @param saveFileName 存在本地的文件名
	 * @param sftp
	 * @throws SftpException 
	 * @throws FileNotFoundException 
	 */
	public static void download(String directory, String downloadFile,String saveFilePath, String saveFileName, ChannelSftp sftp) throws SftpException, FileNotFoundException {
		sftp.cd(directory);
		File file=new File(saveFilePath + saveFileName);
		sftp.get(downloadFile, new FileOutputStream(file));
	}

	/**
	 * 删除文件
	 * @param directory 要删除文件所在目录
	 * @param deleteFile 要删除的文件
	 * @param sftp
	 * @throws SftpException 
	 */
	public static void delete(String directory, String deleteFile, ChannelSftp sftp) throws SftpException {
		sftp.cd(directory);
		sftp.rm(deleteFile);
	}

	/**
	 * 列出目录下的文件
	 * @param directory 要列出的目录
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	@SuppressWarnings("unchecked")
	public static Vector listFiles(String directory, ChannelSftp sftp) throws SftpException{
		return sftp.ls(directory);
	}
}
