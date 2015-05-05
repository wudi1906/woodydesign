package com.mytest.utils.image;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import magick.CompressionType;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import magick.PreviewType;

public class JMagickUtils {

	static {
		System.setProperty("jmagick.systemclassloader", "no");
	}

	public final static int MAX_WIDTH = 800;

	public final static byte IMG_SQUARE = 0;

	public final static byte IMG_OBLONG = 1;

	private int width = 1;

	private int height = 1;

	private ImageInfo info;

	private boolean forceCompress = true;

	private boolean fullQuality;

	private int quality;

	public void setQuality(int quality) {
		this.quality = quality;
	}

	private double rui = 2.0;

	public JMagickUtils(File file, double limitSize) throws ImageException,
			NotPermitImageFormatException, OutOfSizeException {
		long size = file.length();
		BigDecimal decimal = new BigDecimal(size).divide(new BigDecimal(
				1024 * 1024), 1, BigDecimal.ROUND_HALF_UP);
		double amount = decimal.doubleValue() - limitSize;
		if (amount > 0) {
			throw new OutOfSizeException("out of size [ " + decimal.longValue()	+ " ]");
		}
		try {
			setImageInfo(file);
		}
		catch (IOException e) {
			throw new ImageException(e);
		}
		try {
			info = new ImageInfo(file.getAbsolutePath());
			info.setCompression(CompressionType.JPEGCompression);
			info.setPreviewType(PreviewType.JPEGPreview);
		}
		catch (MagickException e) {
			e.printStackTrace();
			throw new ImageException(e);
		}
	}

	public void resizeImage(String newFileName, int size) throws ImageException {
		try {
			MagickImage image = new MagickImage(info);
			try {
				image.profileImage("*", null);
			}
			catch (Exception e1) {// 忽略这个错误
			}
			MagickImage scaled = null;
			double proportion = 0;
			if (width == height) {
				if (width > size) {
					proportion = (double) size / width;
					scaled = image.scaleImage((int) (width * proportion),
							(int) (width * proportion));
				}
				else {
					scaled = image.scaleImage(width, height);
				}
			}
			else {
				if (width > height) {
					if (width > size) {
						proportion = (double) size / width;
						scaled = image.scaleImage(size,
								(int) (height * proportion));
					}
					else {
						scaled = image.scaleImage(width, height);
					}
				}
				else {
					if (height > size) {
						proportion = (double) size / height;
						scaled = image.scaleImage((int) (width * proportion),
								size);
					}
					else {
						scaled = image.scaleImage(width, height);
					}
				}
			}
			scaled.setFileName(newFileName);
			scaled.writeImage(info);
		}
		catch (MagickException e) {
			e.printStackTrace();
			throw new ImageException(e);
		}
	}

	// 120*120的图片,不裁图，只压缩，按照比例
	public void resizeSmall(String newFileName, int size) throws ImageException {
		try {
			if (!this.fullQuality) {
				if (this.quality > 0) {
					info.setQuality(quality);
				}
				else {
					info.setQuality(90);
				}
			}
			MagickImage image = new MagickImage(info);
			try {
				image.profileImage("*", null);
			}
			catch (Exception e1) {// 忽略这个错误
			}
			MagickImage scaled = null;
			double proportion = 0;
			if (width < size && height < size) {
				scaled = image.scaleImage(width, height);
			}
			else {
				if (width == height) {
					scaled = image.scaleImage(size, size);
				}
				else {
					if (width > height) {
						if (width > size) {
							proportion = (double) size / width;
						}
						else {
							proportion = width / (double) size;
						}
						scaled = image.scaleImage(size,
								(int) (height * proportion));
					}
					else {
						if (height > size) {
							proportion = (double) size / height;
						}
						else {
							proportion = height / (double) size;
						}
						scaled = image.scaleImage((int) (width * proportion),
								size);
					}
				}
			}
			if (!this.fullQuality) {
				if (this.rui > 0) {
					scaled = scaled.sharpenImage(1.0, this.rui);
				}
			}
			scaled.setFileName(newFileName);
			scaled.writeImage(info);
		}
		catch (MagickException e) {
			e.printStackTrace();
			throw new ImageException(e);
		}
	}

	public void resizeBig(String newFileName, int size) throws ImageException {
		try {
			if (!this.fullQuality) {
				if (this.quality > 0) {
					info.setQuality(quality);
				}
				else {
					info.setQuality(90);
				}
			}
			MagickImage image = new MagickImage(info);
			try {
				image.profileImage("*", null);
			}
			catch (Exception e1) {// 忽略这个错误
			}
			MagickImage scaled = null;
			double proportion = 0;
			if (width == height) {
				if (width > size) {
					proportion = (double) size / width;
					scaled = image.scaleImage((int) (width * proportion),
							(int) (width * proportion));
				}
				else {
					scaled = image.scaleImage(width, height);
				}
			}
			else {
				if (width > height) {
					if (width > size) {
						proportion = (double) size / width;
						scaled = image.scaleImage(size,
								(int) (height * proportion));
					}
					else {
						scaled = image.scaleImage(width, height);
					}
				}
				else {
					if (height > size) {
						proportion = (double) size / height;
						scaled = image.scaleImage((int) (width * proportion),
								size);
					}
					else {
						scaled = image.scaleImage(width, height);
					}
				}
			}
			if (size > MAX_WIDTH) {
				scaled = scaled.sharpenImage(1.0, 1.0);
			}
			else {
				if (!this.fullQuality) {
					if (this.rui > 0) {
						scaled = scaled.sharpenImage(this.rui, this.rui);
					}
				}
			}
			scaled.setFileName(newFileName);
			scaled.writeImage(info);
		}
		catch (MagickException e) {
			e.printStackTrace();
			throw new ImageException(e);
		}
	}

	/**
	 * 对于600的图片进行处理，限制宽度最宽600，不限制高度
	 * 
	 * @param newFileName
	 * @param size
	 * @throws MagickException
	 */
	public void resizeBigNoLimitHeight(String newFileName, int size,
			int limitWidth) throws MagickException {
		try {
			info.setQuality(100);
			MagickImage image = new MagickImage(info);
			try {
				image.profileImage("*", null);
			}
			catch (Exception e1) {// 忽略这个错误
			}
			MagickImage scaled = null;
			double proportion = 0;
			if (width > limitWidth) {
				if (width == height) {
					if (width > size) {
						proportion = (double) size / width;
						scaled = image.scaleImage((int) (width * proportion),
								(int) (width * proportion));
					}
					else {
						scaled = image.scaleImage(width, height);
					}
				}
				else {
					if (width > height) {
						if (width > size) {
							proportion = (double) size / width;
							scaled = image.scaleImage(size,
									(int) (height * proportion));
						}
						else {
							scaled = image.scaleImage(width, height);
						}
					}
					else {
						if (height > size) {
							proportion = (double) size / height;
							scaled = image.scaleImage(
									(int) (width * proportion), size);
						}
						else {
							scaled = image.scaleImage(width, height);
						}
					}
				}
			}
			else {
				scaled = image;
			}
			if (size > MAX_WIDTH) {
				scaled = scaled.sharpenImage(1.0, 1.0);
			}
			else {
				if (!this.fullQuality) {
					if (this.rui > 0) {
						scaled = scaled.sharpenImage(1.0, this.rui);
					}
				}
			}
			scaled.setFileName(newFileName);
			scaled.writeImage(info);
		}
		catch (MagickException e) {
			throw new MagickException("failed resize file ["
					+ info.getFileName() + "]");
		}
	}

	/**
	 * 方图 ,裁减后压缩<br/>
	 * 从上传的大图中裁剪出中间部分，然后对中间部分进行压缩
	 */
	public void makeSquare(String newFileName, int size) throws ImageException {
		try {
			Rectangle rect = null;
			if (width < size && height < size) {
				rect = new Rectangle(0, 0, size, size);
			}
			else if (width < size || height < size) {
				rect = new Rectangle(0, 0, size, size);
			}
			else {
				if (width < height) {
					int beginPoint = (height - width) / 2;
					rect = new Rectangle(0, beginPoint, width, width);
				}
				else {
					int beginPoint = (width - height) / 2;
					rect = new Rectangle(beginPoint, 0, height, height);
				}
			}
			if (!this.fullQuality) {
				info.setQuality(95);
			}
			MagickImage image = new MagickImage(info);
			try {
				image.profileImage("*", null);
			}
			catch (Exception e1) {// 忽略这个错误
			}
			MagickImage cropped = image.cropImage(rect).scaleImage(size, size);
			if (!this.fullQuality) {
				if (this.rui > 0) {
					cropped = cropped.sharpenImage(1.0, this.rui);
				}
			}
			cropped.setFileName(newFileName);
			cropped.writeImage(info);
		}
		catch (MagickException e) {
			e.printStackTrace();
			throw new ImageException(e);
		}
	}

	public void cutImage(String filePath, String fileName, int x1, int y1,
			int x2, int y2) throws ImageException {
		File f = new File(filePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		String newFile = filePath + fileName;
		int new_width = x2 - x1;
		int new_height = y2 - y1;
		try {
			Rectangle rect = new Rectangle(x1, y1, new_width, new_height);
			info.setQuality(95);
			MagickImage image = new MagickImage(info);
			try {
				image.profileImage("*", null);
			}
			catch (Exception e1) {// 忽略这个错误
			}
			MagickImage cropped = image.cropImage(rect);
			cropped.setFileName(newFile);
			cropped.writeImage(info);
		}
		catch (MagickException e) {
			e.printStackTrace();
			throw new ImageException(e);
		}
	}

	/**
	 * @param filePath 图片目录
	 * @param fileName 图片名称,包括扩展名
	 * @param type 图片几何类型
	 * @param size 图片尺寸
	 * @throws MagickException
	 */
	public void makeImage(String filePath, String fileName, byte type, int size)
			throws ImageException {
		File f = new File(filePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		String newFile = filePath + fileName;
		String img = newFile.substring(0, newFile.lastIndexOf("."));
		// 文件先不加后缀
		if (type == JMagickUtils.IMG_SQUARE) {
			this.makeSquare(img, size);
		}
		else if (type == JMagickUtils.IMG_OBLONG) {
			if (size <= 120) {
				this.resizeSmall(img, size);
			}
			else if (size > 120) {
				this.resizeBig(img, size);
			}
		}
		File old_file = new File(newFile);
		if (old_file.isFile()) {
			old_file.delete();
		}
		File file = new File(img);
		file.renameTo(new File(newFile));
	}

	/**
	 * 制作边长大于500的图片
	 * 
	 * @param filePath
	 * @param fileName
	 * @param type
	 * @param size
	 * @param limitWidth
	 * @throws MagickException
	 */
	public void makeBigImage(String filePath, String fileName, int size,
			int limitWidth) throws MagickException {
		File f = new File(filePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		String newFile = filePath + fileName;
		this.resizeBigNoLimitHeight(newFile, size, limitWidth);
	}

	private void setImageInfo(Object input) throws IOException,
			NotPermitImageFormatException {
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(input);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				reader.setInput(iis);
				String imgType = reader.getFormatName();
				if (imgType.equalsIgnoreCase("JPEG")
						|| (imgType.equalsIgnoreCase("gif"))
						|| imgType.equalsIgnoreCase("png")) {
					width = reader.getWidth(0);
					height = reader.getHeight(0);
					if (iis != null) {
						iis.close();
						iis = null;
					}
				}
				else {
					throw new NotPermitImageFormatException(
							"no jpeg , gif , png");
				}
			}
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (iis != null) {
				iis.close();
				iis = null;
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ImageInfo getInfo() {
		return info;
	}

	public void setInfo(ImageInfo info) {
		this.info = info;
	}

	public static int getMAX_WIDTH() {
		return MAX_WIDTH;
	}

	public static byte getIMG_SQUARE() {
		return IMG_SQUARE;
	}

	public static byte getIMG_OBLONG() {
		return IMG_OBLONG;
	}

	/**
	 * 查看图片是不是程序所允许的类型
	 * 
	 * @param file
	 * @param access_imgType
	 * @return
	 * @throws IOException
	 */
	public static boolean accessImage(File file, String[] access_imgType)
			throws IOException {
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				reader.setInput(iis);
				String imgType = reader.getFormatName().toLowerCase();
				for (String t : access_imgType) {
					if (imgType.equals(t.toLowerCase())) {
						return true;
					}
				}
			}
			return false;
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (iis != null) {
				iis.close();
				iis = null;
			}
		}
	}

	public void setRui(double rui) {
		this.rui = rui;
	}

	public double getRui() {
		return rui;
	}

	public void setFullQuality(boolean fullQuality) {
		this.fullQuality = fullQuality;
	}

	public void setForceCompress(boolean forceCompress) {
		this.forceCompress = forceCompress;
	}

	public boolean isForceCompress() {
		return forceCompress;
	}
}