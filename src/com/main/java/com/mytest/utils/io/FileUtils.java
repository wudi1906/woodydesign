package com.mytest.utils.io;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import client.FileClient;
import client.UploadFile;
import client.UploadResult;

import com.mytest.utils.image.JMagickUtils;

/**
 * @创建者 周仕军
 * @时间 Dec 8, 2008, 2:33:33 PM
 * @描述
 */
public class FileUtils {

	/**
	 * @创建者 刘洋
	 * @时间 Dec 8, 2008, 2:00:11 PM
	 * @描述 把上传的文件复制到指定目录下并重新命名
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public static void copy(File src, File dst, int bufferSize)
			throws IOException {
		if (!dst.getParentFile().exists()) {
			dst.getParentFile().mkdirs();
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), bufferSize);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					bufferSize);
			byte[] buffer = new byte[bufferSize];
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
		} finally {
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * @创建者 刘洋
	 * @时间 Jan 15, 2009, 5:16:25 PM
	 * @描述 按比例缩小，如果图片高度和宽度都比要求的小则不缩小
	 * @param fromFile
	 * @param toFile
	 * @param maxWidth
	 *            最大宽度
	 * @param maxHeight
	 *            最大高度
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static void scaling(File fromFile, File toFile, int maxWidth,
			int maxHeight) throws Exception {
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		double ratio = 1.0;
		if (!fromFile.isFile())
			throw new Exception(fromFile.getName()
					+ " is not image file error in CreateThumbnail!");
		BufferedImage bi = ImageIO.read(fromFile);
		// 假设图片宽 高
		Image Itemp = bi
				.getScaledInstance(maxWidth, maxHeight, bi.SCALE_SMOOTH);
		if ((bi.getHeight() > maxHeight) || (bi.getWidth() > maxWidth)) {
			if (bi.getHeight() > bi.getWidth()) {
				ratio = (double) maxHeight / bi.getHeight();
			} else {
				ratio = (double) maxWidth / bi.getWidth();
			}
		}
		AffineTransformOp op = new AffineTransformOp(AffineTransform
				.getScaleInstance(ratio, ratio), null);
		Itemp = op.filter(bi, null);
		try {
			ImageIO.write((BufferedImage) Itemp, "jpg", toFile);
		} catch (Exception ex) {
			throw new Exception(" ImageIo.write error in CreatThum.: "
					+ ex.getMessage());
		}
	}

	/**
	 * @创建者 李天石
	 * @时间 20110303
	 * @描述 使用JMagic按比例缩小，如果图片高度和宽度都比要求的小则不缩小
	 * @param fromFile
	 * @param toFile
	 * @param maxWidth
	 *            最大宽度
	 * @param maxHeight
	 *            最大高度
	 * @throws Exception
	 */
	public static void scalingByJMagic(String fromFilePath, String toPath,
			String fileName, int maxWidth, int maxHeight) throws Exception {
		// 假设图片宽 高
		int size = 0;
		if (maxWidth > maxHeight) {
			size = maxWidth;
		} else {
			size = maxHeight;
		}
		try {
			JMagickUtils util = new JMagickUtils(new File(fromFilePath), 10);
			util.setRui(0);
			util.makeImage(toPath, fileName, JMagickUtils.IMG_OBLONG, size);
		} catch (Exception ex) {
			throw new Exception(" JMagick.write error in CreatThum.: "
					+ ex.getMessage());
		}
	}

	/**
	 * @创建者 李天石
	 * @时间 Dec 28, 2010
	 * @描述 按照指定尺寸缩放图片
	 * @param fromFile
	 * @param toFile
	 * @param width
	 *            最大宽度
	 * @param height
	 *            最大高度
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static void reSize(File fromFile, File toFile, int width, int height)
			throws Exception {
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		double ratiox = 1.0;
		double ratioy = 1.0;
		if (!fromFile.isFile())
			throw new Exception(fromFile.getName()
					+ " is not image file error in CreateThumbnail!");
		BufferedImage bi = ImageIO.read(fromFile);
		// 假设图片宽 高
		Image Itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
		ratioy = (double) height / bi.getHeight();
		ratiox = (double) width / bi.getWidth();
		AffineTransformOp op = new AffineTransformOp(AffineTransform
				.getScaleInstance(ratiox, ratioy), null);
		Itemp = op.filter(bi, null);
		try {
			ImageIO.write((BufferedImage) Itemp, "jpg", toFile);
		} catch (Exception ex) {
			throw new Exception(" ImageIo.write error in CreatThum.: "
					+ ex.getMessage());
		}
	}

	/**
	 * @创建者 刘洋
	 * @时间 Jan 15, 2009, 10:43:17 AM
	 * @描述 后缀
	 * @param fileName
	 * @return
	 */
	public static String getPostfix(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 取主文件名(文件名种不包括扩展名的部分)
	 */
	public static String getPrefix(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	public static String getPrefix(String fileName, String signal) {
		return fileName.substring(0, fileName.lastIndexOf(".")) + signal;
	}

	/**
	 * 获得文件名
	 * 
	 * @param path
	 *            文件绝对路径
	 * @return
	 */
	public static String getFileName(String path) {
		if (path == null)
			return null;
		Pattern p = Pattern.compile(".+\\\\(.+)$");
		Matcher m = p.matcher(path);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	/**
	 * @创建者 刘洋
	 * @时间 Jan 8, 2009, 5:31:13 PM
	 * @描述 只允许删除文件，不允许删除文件夹
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		if (null != filePath && !"".equals(filePath)) {
			File file = new File(filePath);
			if (!file.isDirectory()) {
				file.delete();
			}
		}
	}

	/**
	 * @创建者 刘洋
	 * @时间 2010-1-10 下午09:32:42
	 * @描述 删除文件夹(及其下面所有文件)
	 * @param folderPath
	 *            文件夹完整绝对路径
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @创建者 刘洋
	 * @时间 2010-1-10 下午09:36:11
	 * @描述 删除指定文件夹下所有文件
	 * @param path
	 *            文件夹完整绝对路径
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 生成图片名称
	 */
	public static String generateImgFileName(String pictureFileFileName) {
		String imageFileName = System.currentTimeMillis()
				+ FileUtils.getPostfix(pictureFileFileName);
		return imageFileName;
	}

	/**
	 * 生成图片路径
	 * 
	 * @return
	 */
	public static String generateImgFilePath() {
		Calendar createDate = Calendar.getInstance();
		String year = String.valueOf(createDate.get(Calendar.YEAR));
		String month = String.valueOf(createDate.get(Calendar.MONTH) + 1);
		StringBuilder filePath = new StringBuilder();
		// filePath.append(ImageProperty.FOLDER_OF_ADVERTISEMENT_ICON);
		filePath.append("/");
		filePath.append(year);
		filePath.append("/");
		filePath.append(month);
		filePath.append("/");
		return filePath.toString();
	}

	/**
	 * 生成宝箱使用图片路径
	 * 
	 * @return
	 */
	public static String generateBoxImgFilePath() {
		Calendar createDate = Calendar.getInstance();
		String year = String.valueOf(createDate.get(Calendar.YEAR));
		String month = String.valueOf(createDate.get(Calendar.MONTH) + 1);
		StringBuilder filePath = new StringBuilder();
		// filePath.append(ImageProperty.FOLDER_OF_BOX_ICON);
		filePath.append("/");
		filePath.append(year);
		filePath.append("/");
		filePath.append(month);
		filePath.append("/");
		return filePath.toString();
	}

	public static void writeCSVFile(File csvfile, List<String[]> datas) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(csvfile, true)); // 附加
			StringBuffer sbtemp = null;
			if (datas != null && datas.size() > 0) {
				for (String[] ss : datas) {
					sbtemp = new StringBuffer();
					for (String s : ss) {
						sbtemp.append(",");
						sbtemp.append("\"");
						sbtemp.append(s);
						sbtemp.append("\"");
					}
					if (",".equals(sbtemp.charAt(0) + "")) {
						sbtemp.deleteCharAt(0);
					}
					System.out.println(sbtemp.toString());
					bw.write(sbtemp.toString());
					bw.newLine();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("生成csv文件失败" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("生成csv文件失败" + e.getMessage());
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public static void writeCSVFile(OutputStreamWriter csvout,
			List<String[]> datas) {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(csvout); // 附加
			StringBuffer sbtemp = null;
			if (datas != null && datas.size() > 0) {
				for (String[] ss : datas) {
					sbtemp = new StringBuffer();
					for (String s : ss) {
						sbtemp.append(",");
						sbtemp.append("\"");
						sbtemp.append(s);
						sbtemp.append("\"");
					}
					if (",".equals(sbtemp.charAt(0) + "")) {
						sbtemp.deleteCharAt(0);
					}
					System.out.println(sbtemp.toString());
					bw.write(sbtemp.toString());
					bw.newLine();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("生成csv文件失败" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("生成csv文件失败" + e.getMessage());
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public static List<UploadResult> getUploadResults(String diskPath,
			String imgFileName, FileClient fileClient, String... boxImgConf)
			throws Exception {
		List<UploadFile> uploadFiles = new ArrayList<UploadFile>();
		List<File> files = new LinkedList<File>();
		int i = 0;
		for (String s : boxImgConf) {
			File file = new File(diskPath + imgFileName + s);
			UploadFile uploadFile = new UploadFile("file" + i, file,
					(imgFileName + s));
			uploadFiles.add(uploadFile);
			files.add(file);
			i++;
		}
		List<UploadResult> list = fileClient.upload(null, true, uploadFiles);
		for (File f : files) {
			f.delete();
		}
		return list;
	};

	/**
	 * 文件大小
	 * 
	 * @param file
	 *         源文件
	 * @return 文件大小
	 */
	public static long sizeOf(File file) {
		return org.apache.commons.io.FileUtils.sizeOf(file);
	}	
	
	public static void main(String[] args) {
		// String diskPath = "d:/1.jpg";
		// File f = new File(diskPath);
		// try {
		// System.out.println(f.getParent());
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		File file = new File("D:/test.csv");
		String[] s = { "1111", "1112", "1113" };
		String[] s1 = { "2111", "2112", "2113" };
		List<String[]> datas = new ArrayList<String[]>();
		datas.add(s);
		datas.add(s1);
		writeCSVFile(file, datas);
	}
}
