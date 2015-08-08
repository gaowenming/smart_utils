/*
 ****************************************************************************
 * 功能: 图片增加水印工具类													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年1月14日 下午5:43:21 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年1月14日    |    gaowenming     |     Created 
 */
package com.smart.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

/**
 * Description: <图片加水印工具类>. <br>
 * <p>
 * <使用说明>
 * </p>
 * Makedate:2014年1月14日 下午5:43:21
 * 
 * @version V1.0
 */

public class ImageUtils {

	public static Integer TYPE_TEXT = 0;
	public static Integer TYPE_IMAGE = 1;

	static Logger logger = Logger.getLogger(ImageUtils.class);

	private static void drawImage(Graphics2D g, String iconPath,
			Integer buffImgWidth, Integer buffImgHeight, Float clarity) {
		// 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
		ImageIcon imgIcon = new ImageIcon(iconPath);
		int width = imgIcon.getIconWidth();
		int height = imgIcon.getIconHeight();
		int larger1 = width;
		if (height > width) {
			larger1 = height;
		}

		int larger2 = buffImgWidth;
		if (buffImgHeight < buffImgWidth) {
			larger2 = buffImgHeight;
		}
		int scale = 100;
		if (larger1 < larger2) {
			// 求得不超出原是图片最小边的最大缩放比例-10 (缩放比例10进制)
			scale = (larger2 - 10) / larger1 * 100 / 10 * 10;
		}

		BufferedImage buffImgIcon = zoomImage(iconPath, scale);

		float alpha = clarity; // 透明度
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				alpha));

		int logoTextx, logoTexty;
		// logotext默认上下左右居中显示
		logoTextx = (buffImgWidth - buffImgIcon.getWidth()) / 2;
		logoTexty = (buffImgHeight - buffImgIcon.getHeight()) / 2;
		// 表示水印图片的位置
		g.drawImage(buffImgIcon, logoTextx, logoTexty, null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		g.dispose();

	}

	private static void drawString(Graphics2D g, String logoText,
			Integer buffImgWidth, Integer buffImgHeight, Float clarity) {

		// 创建字体
		Font font = null;
		// 将字体的大小设置为刚好比buggimg的宽度少一个字号
		for (int i = 1; i < 1000; i++) {
			font = new Font("宋体", Font.BOLD, i);
			// 设置 Font
			g.setFont(font);
			// 得到字体渲染上下文
			FontRenderContext context = g.getFontRenderContext();
			// 获取字体的像素范围对象
			Rectangle2D stringBounds = font.getStringBounds(logoText, context);
			int width = Integer.parseInt(new java.text.DecimalFormat("0")
					.format(stringBounds.getWidth()));
			if (buffImgWidth < width) {
				font = new Font("宋体", Font.BOLD, i - 1);
				logger.debug("i:" + i + " -->buffImgWidth:" + buffImgWidth
						+ " : " + width);
				break;
			}
		}
		g.setFont(font);
		// 得到字体渲染上下文
		FontRenderContext context = g.getFontRenderContext();
		// 获取字体的像素范围对象
		Rectangle2D stringBounds = font.getStringBounds(logoText, context);
		// 设置文字透明度变量
		float alpha = clarity;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				alpha));
		int logoTextx, logoTexty;
		// logotext默认上下左右居中显示
		logoTextx = (buffImgWidth - Integer
				.parseInt(new java.text.DecimalFormat("0").format(stringBounds
						.getWidth()))) / 2;
		logoTexty = (buffImgHeight - Integer
				.parseInt(new java.text.DecimalFormat("0").format(stringBounds
						.getHeight()))) / 2;
		// 第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y) .
		g.drawString(logoText, logoTextx, logoTexty);
		g.dispose();
	}

	/**
	 * 
	 * 描述 : <图片添加水印功能,水印可以是文字或者图片>. <br>
	 * <p>
	 * 
	 * 图片都会按照png格式输出 请注意
	 * <水印居中显示 中心对称旋转, 水印文字或者水印图片 会被按比例缩放,
	 * 水印的最长边所放到原始图片的最短边小一点(因为图片水印和文字水印缩放的修正值可能有偏差)>
	 * </p>
	 * 
	 * @param waterMarkContent
	 *            水印文字或者水印图片地址
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 * @param degree
	 *            水印图片旋转角度
	 * @param clarity
	 *            透明度(小于1的数)越接近0越透明 默认为0.2
	 * @param type
	 *            CP_WatermarkUtils.TYPE_IMAGE:图片水印
	 *            CP_WatermarkUtils.TYPE_TEXT:文字水印
	 */
	public static void waterMark(String waterMarkContent, String srcImgPath,
			String targerPath, Integer degree, Float clarity, Integer type) {
		if (!new File(srcImgPath).exists()) {
			logger.error("CP_WatermarkUtils -> waterMarImagekByText异常:图片源文件不存在!");
			return;
		}
		if (clarity == null) {
			clarity = 0.2f;
		}
		// 主图片的路径
		InputStream is = null;
		OutputStream os = null;
		try {
			// 根据路径获得image对象
			Image srcImg = ImageIO.read(new File(srcImgPath));

			// 根据image创建bufferedimage对象
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
					srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

			int buffImgWidth = buffImg.getWidth();
			int buffImgHeight = buffImg.getHeight();
			// 得到buffimg的图形对象
			Graphics2D g = buffImg.createGraphics();

			// 设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			// 将srcImage原图像 画到buffimg中
			g.drawImage(
					srcImg.getScaledInstance(srcImg.getWidth(null),
							srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
					null);

			if (null != degree) {
				// 设置水印旋转
				g.rotate(Math.toRadians(degree), (double) buffImgWidth / 2,
						(double) buffImgHeight / 2);
			}

			// 设置颜色
			g.setColor(Color.red);

			if (type == TYPE_TEXT) {
				drawString(g, waterMarkContent, buffImgWidth, buffImgHeight,
						clarity);
			} else if (type == TYPE_IMAGE) {
				drawImage(g, waterMarkContent, buffImgWidth, buffImgHeight,
						clarity);
			}
			os = new FileOutputStream(targerPath);

			// 生成图片
			ImageIO.write(buffImg, "PNG", os);

			logger.info("图片添加文字水印完成!");
		} catch (Exception e) {
			logger.error("CP_WatermarkUtils --> waterMarImagekByText图片添加水印异常:",
					e);
		} finally {
			try {
				if (null != is)
					is.close();
			} catch (Exception e) {
				logger.error(
						"CP_WatermarkUtils --> waterMarImagekByText关闭流异常:", e);
			}
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				logger.error(
						"CP_WatermarkUtils --> waterMarImagekByText关闭流异常:", e);
			}
		}
	}

	/**
	 * 
	 * 描述 : <图片添加图片水印功能>. <br>
	 * <p>
	 * 图片都会按照png格式输出 请注意
	 * <水印居中显示 中心对称旋转, 水印图片 会被按比例缩放,
	 * 水印的最长边所放到原始图片的最短边小一点(因为图片水印和文字水印缩放的修正值可能有偏差)>
	 * </p>
	 * 
	 * @param waterMarkContent
	 *            水印图片地址
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 * @param degree
	 *            水印图片旋转角度
	 * @param clarity
	 *            透明度(小于1的数)越接近0越透明 默认为0.2
	 *            
	 */
	public static void waterMarkByImage(String waterMarkContent, String srcImgPath,
			String targerPath, Integer degree, Float clarity) {
		waterMark(waterMarkContent, srcImgPath, targerPath, degree, clarity,ImageUtils.TYPE_IMAGE);
	}
	/**
	 * 
	 * 描述 : <图片添加图片水印功能>. <br>
	 * <p>
	 * 图片都会按照png格式输出 请注意
	 * <水印居中显示 中心对称旋转, 水印图片 会被按比例缩放,
	 * 水印的最长边所放到原始图片的最短边小一点(因为图片水印和文字水印缩放的修正值可能有偏差)
	 * 默认旋转角度为水平
	 * 默认透明度为0.2f
	 * >
	 * </p>
	 * 
	 * @param waterMarkContent
	 *            水印图片地址
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 */
	public static void waterMarkByImage(String waterMarkContent, String srcImgPath,
			String targerPath) {
		waterMark(waterMarkContent, srcImgPath, targerPath, null, null,ImageUtils.TYPE_IMAGE);
	}
	/**
	 * 
	 * 描述 : <图片添加文字水印功能>. <br>
	 * <p>
	 * 图片都会按照png格式输出 请注意
	 * <水印居中显示 中心对称旋转, 水印文字 会被按比例缩放,
	 * 水印的最长边所放到原始图片的最短边小一点(因为图片水印和文字水印缩放的修正值可能有偏差)>
	 * </p>
	 * 
	 * @param waterMarkContent
	 *            水印文字
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 * @param degree
	 *            水印文字旋转角度
	 * @param clarity
	 *            透明度(小于1的数)越接近0越透明 默认为0.2
	 *            
	 */
	public static void waterMarkByText(String waterMarkContent, String srcImgPath,
			String targerPath, Integer degree, Float clarity) {
		waterMark(waterMarkContent, srcImgPath, targerPath, degree, clarity,ImageUtils.TYPE_TEXT);
	}
	/**
	 * 
	 * 描述 : <图片添加文字水印功能>. <br>
	 * <p>
	 * 图片都会按照png格式输出 请注意
	 * <水印居中显示 中心对称旋转, 水印文字 会被按比例缩放,
	 * 水印的最长边所放到原始图片的最短边小一点(因为图片水印和文字水印缩放的修正值可能有偏差)>
	  * 默认旋转角度为水平
	 * 默认透明度为0.2f
	 * </p>
	 * 
	 * @param waterMarkContent
	 *            水印文字
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 *            
	 */
	public static void waterMarkByText(String waterMarkContent, String srcImgPath,
			String targerPath) {
		waterMark(waterMarkContent, srcImgPath, targerPath, null, null,ImageUtils.TYPE_TEXT);
	}
	/**
	 * 
	 * 描述 : <按比例缩放图像>. <br>
	 * <p>
	 * 图片都会按照png格式输出 请注意
	 * <使用方法说明>
	 * </p>
	 * 
	 * @param srcPath
	 *            图片路径
	 * @param destPath
	 *            缩放后图片存放路径
	 * @param scale
	 *            缩放精度 底数为100, 例如:100为原始大小 200为放大两倍 50为缩小一倍
	 * @throws IOException
	 */
	public static void zoomImage(String srcPath, String destPath, int scale)
			throws IOException {
		ImageIO.write(zoomImage(srcPath, scale), "PNG", new File(destPath));
	}

	/**
	 * 
	 * 描述 : <按比例缩放图像>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * 
	 * @param srcPath
	 *            图片路径
	 * @param scale
	 *            缩放精度 底数为100, 例如:100为原始大小 200为放大两倍 50为缩小一倍
	 * @throws IOException
	 * @return BufferedImage
	 */
	public static BufferedImage zoomImage(String srcPath, int scale) {
		try {
			BufferedImage srcBufferedImage = ImageIO.read(new File(srcPath));
			if (scale == 100) {
				return srcBufferedImage;
			}

			int destWidth = (int) (srcBufferedImage.getWidth(null) * scale / 100.0);
			int destHeight = (int) (srcBufferedImage.getHeight(null) * scale / 100.0);

			BufferedImage destBufferedImage = new BufferedImage(destWidth,
					destHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = destBufferedImage.createGraphics();
			graphics2D.setBackground(Color.WHITE);
			graphics2D.clearRect(0, 0, destWidth, destHeight);
			graphics2D.drawImage(srcBufferedImage.getScaledInstance(destWidth,
					destHeight, Image.SCALE_SMOOTH), 0, 0, null);
			graphics2D.dispose();
			return destBufferedImage;
		} catch (IOException e) {
			logger.error("CP_WatermarkUtils -> zoomImage 异常:", e);
		}
		return null;
	}

	public static void main(String[] args) {

		String srcImgPath = "src/test/resources/1.png";
		String logoText = "[ 测试文字水印 http://www.nq.com ]";
		String targetPathOfText1 = "src/test/resources/textTest1.png";
		String targetPathOfText2 = "src/test/resources/textTest2.png";
		String logoIcon = "src/test/resources/nq.png";
		ImageUtils.waterMark(logoText, srcImgPath, targetPathOfText1,
				-45, 0.2f, ImageUtils.TYPE_TEXT);

		ImageUtils.waterMark(logoText, srcImgPath, targetPathOfText2, 0,
				0.2f, ImageUtils.TYPE_TEXT);

		String targerPathOfIcon1 = "src/test/resources/iconTest1.png";
		String targerPathOfIcon2 = "src/test/resources/iconTest2.png";
		ImageUtils.waterMark(logoIcon, srcImgPath, targerPathOfIcon1,
				-45, 0.2f, ImageUtils.TYPE_IMAGE);

		ImageUtils.waterMark(logoIcon, srcImgPath, targerPathOfIcon2, 0,
				0.2f, ImageUtils.TYPE_IMAGE);
	}

}