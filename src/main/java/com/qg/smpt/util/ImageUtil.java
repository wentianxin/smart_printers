package com.qg.smpt.util;

import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.Arrays;

public class ImageUtil {
    private static final Logger LOGGER = Logger.getLogger(ImageUtil.class);

    public static byte[] getImage(String filename)  {
        File file = new File(filename);
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();

		LOGGER.log(Level.DEBUG, "图片的宽为[{0}],长为[{1}]",width, height);

        int[][] data = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i);
                data[i][j] = rgb;
            }
        }

        return getDatagram(data);
    }


    /**
     *
     */
    public static boolean transformBinaryImage(InputStream inputStream, String filepath) {
        try {
            LOGGER.log(Level.INFO, "正在进行将图片黑白化，并将保存到[{0}]", filepath);

            if(inputStream == null) {
                LOGGER.log(Level.ERROR,"要转化的图片数据为空，转化失败");
                return false;
            }

            BufferedImage image = ImageIO.read(inputStream);

            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

            byte[][] bytes = new byte[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
                    bytes[i][j] = (byte) rgb;
                    rgb = twoValue(grey((byte) rgb));
                    grayImage.setRGB(i, j, rgb);
                }
            }

            File newFile = new File(filepath);
            return ImageIO.write(grayImage, "jpg", newFile);

        }catch (Exception e) {
            LOGGER.log(Level.ERROR, "转化图片的过程出现了错误，错误原因是[{0}]", e);
            return false;
        }
    }


	/**
	 * 将RGB图片变为压缩后的数据
	 */
	public static byte[] getDatagram(int[][] pixels) {
	    LOGGER.log(Level.INFO, "正在进行将图片压缩");
		return compress(rgbToBitmap(pixels), pixels.length, pixels[0].length);
	}

	/**
	 * 用行程编码的方式对位图进行压缩
	 *
	 * @param src 未压缩前数据
	 * @param row 打印数据的行数
	 * @param col 打印数据的列数
	 * @return 压缩后的数据
	 */
	public static byte[] compress(byte[] src, int row, int col) {
		byte[] des = new byte[src.length];
		int count = 0;
		int map = 0x80;

		// 用索引代替指针
		int s = 0;
		int d = 0;

	/*压缩完毕后的数组头两个字节代表行数、列数，其余字节为压缩的图像数据*/
		des[d++] = (byte) row;
		des[d++] = (byte) (col / 8 );
		while (true) {
            /*测试连续的1*/
			while ((src[s] & map) != 0) {
				if (count++ == 127) { //des存满一个字节
					des[d++] = (byte) 0xff;
					count = 0;
				} else if ((map >>= 1) == 0) { //src测试完一个字节
//                    if (++src == src_end + 1)    //假如src已经转换完毕,跳出循环
					s++;
					if (s == src.length)    //假如src已经转换完毕,跳出循环
						break;
					map = 0x80;
				}
			}

			if (count != 0) {
				des[d++] = (byte) (128 + count);
				count = 0;
			}


//            if (src == src_end + 1)//假如src已经转换完毕,跳出循环
			if (s == src.length)//假如src已经转换完毕,跳出循环
				break;
            /*测试连续的0*/
//            while (!(* src) & map)){
			while ((src[s] & map) == 0) {
				if (count++ == 127) {  //des存满一个字节
					des[d++] = 127;
					count = 0;
				} else if ((map >>= 1) == 0) { //src测试完一个字节
//                    if (++src == src_end + 1)    //假如src已经转换完毕,跳出循环
					s++;
					if (s == src.length)    //假如src已经转换完毕,跳出循环
						break;

					map = 0x80;
				}
			}

			if (count != 0) {
				des[d++] = (byte) count;
				count = 0;
			}

//            if (src == src_end + 1) { //假如src已经转换完毕,跳出循环
			if (s == src.length) {//假如src已经转换完毕,跳出循环
				break;
			}
		}
		LOGGER.log(Level.INFO,"压缩前字节数：[{0}],   压缩后字节数：[{1}],   压缩率为：[{2}]\n",
				src.length, d, 100 - (int) ((float) d / (float) src.length * 100.0));

		return Arrays.copyOfRange(des, 0, d);
	}

	/**
	 * rgb转1位二值图
	 */
	public static final byte[] rgbToBitmap(int[][] rgbPixels) {
		return binary(rgbToBW(rgbPixels));
	}

	/**
	 * rgb转8位二值图
	 */
	public static byte[][] rgbToBW(int[][] rgbPixels) {
		return twoValue(grey(rgbPixels));
	}
	/**
	 * rgb点转8位二值点
	 */
	public static byte rgbToBW(int rgbPixel) {
		return twoValue(grey(rgbPixel));
	}

	/**
	 * 8位二值图图转1位二值图
	 */
	public static final byte[] binary(byte[][] pixels) {
		final int length = pixels.length * pixels[0].length;
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(length);

		final int START = 0b10000000; // 第8位开始

		int index = START;
		byte tempByte = 0;
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				byte b = pixels[i][j];
				if ((b & 0xff) == 255) {
					tempByte |= 0;
				} else {
					tempByte |= index;
				}
				index >>>= 1;
				if (index == 0) {
					index = START;
					baos.write(tempByte);
					tempByte = 0;
				}
			}
		}
		return baos.toByteArray();
	}

	/**
	 * 灰度图转8位二值图(255-白， 0-黑)
	 */
	public static final byte[][] twoValue(byte[][] pixels) {
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				pixels[i][j] = twoValue(pixels[i][j]);
			}
		}
		return pixels;
	}


	/**
	 * 灰度图像素点转二值像素点
	 */
	public static final byte twoValue(byte pixel) {

//        System.out.format("pixel%d, pixel & 0xff : %d\n", (int) pixel, pixel & 0xff);
		// 阈值法, 阈值为128
		return (byte) ((pixel & 0xff) > 128 ? 255 : 0);
	}

	/**
	 * RGB图转灰度图
	 */
	public static final byte[][] grey(int[][] pixels) {
		byte[][] bytes = new byte[pixels.length][pixels[0].length];
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				bytes[i][j] = grey(pixels[i][j]);
			}
		}
		return bytes;
	}

	/**
	 * RGB像素点转灰度数值
	 */
	public static final byte grey(int pixel) {
		int red = red(pixel);
		int green = green(pixel);
		int blue = blue(pixel);
		byte b = (byte) (red * 0.3 + green * 0.59 + blue * 0.11);
		return b;
	}

	public static int red(int color) {
		return (color >> 16) & 0xFF;
	}

	public static int green(int color) {
		return (color >> 8) & 0xFF;
	}

	public static int blue(int color) {
		return color & 0xFF;
	}

}