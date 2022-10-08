package com.qrcode.qrcodeapp.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class QRmain_rorate {
	private static int version, el, em, kai;
	private static byte masknum;
	private static String originalfilename;
	private static String imagefilename;
	private static String textdata;
	public static String FilePath;
	public static byte[][] des, weightmod;
	public static double[][] Yuv, yUv, yuV;
	private static byte[][] s;
	private static int Ave, mod, modules1Side;
	private static int Th;
	private static int msize;
	private static double var;

	private static double scale;
	private static int side1;
	private static int side1clone;
	private static int original_width;
	private static int original_height;
	private static int clip_x, clip_y;
	private static double y_position, x_position;

	public static void main(final String[] args) {
		parseOptions(args);
		modules1Side = 4*version + 17;
		mod = 0; // non-systematic encoding

		trimming(originalfilename);
		File trimming = new File("./tmp/trimming.png");
		imagefilename = trimming.getPath();
		
		binarize(imagefilename);
		weightingBackGround(imagefilename);
		weightingFace(imagefilename);
		Qrcode x = new Qrcode();
		x.setQrcodeVersion(version);
		x.setQrcodekaisu(kai);
		x.setQrcodeErrorCorrect(el);
		x.setQrcodeEncodeMode(em);
		x.setQrcodemask(masknum);
		x.setQrcodeMode(mod);
		byte[] d = textdata.getBytes();
		s = x.calQrcode(d);
		makefile(s);

		File QRcode = new File("./tmp/QRcode.png");
		insert(QRcode.getPath(), originalfilename);

	}

	private static void parseOptions(final String[] args) {
		if (args.length != 12) {
			System.out.println(
					"[input image] [data] [version] [ECC level] [encoding] [mask pattern] [trial] [Th] [scale] [var] [y_position] [x_position]");
			System.out.println("version: 1 - 40");
			System.out.println("ECC level:  0:L 1:M 2:Q 3:H");
			System.out.println("encoding:  0:8 bits (byte)  1:alphabet  others: number");
			System.out.println("mask pattern: 0 - 7");
			System.out.println("trial: number of candidates");
			System.out.println("Th: threshold for module intensity");
			System.out.println("X: scaling");
			System.out.println("var: varince of module (Gaussian shape)");
			System.out.println("y_position");
			System.out.println("x_position");
			System.exit(1);
		}
		originalfilename = args[0];
		textdata = args[1];
		version = Integer.parseInt(args[2]);
		el = Integer.parseInt(args[3]);
		em = Integer.parseInt(args[4]);
		masknum = Byte.parseByte(args[5]);
		kai = Integer.parseInt(args[6]);
		Th = Integer.parseInt(args[7]);
		scale = Double.parseDouble(args[8])/100.0;
		var = Double.parseDouble(args[9]);
		y_position = Double.parseDouble(args[10]);
		x_position = Double.parseDouble(args[11]);
	}

	public static void binarize(String file) {
		File f = new File(file);
		Mat img = Imgcodecs.imread(f.getAbsolutePath());
		if (img == null) {
			throw new IllegalArgumentException("Illegal input file.");
		}

		int w = msize * modules1Side;// System.out.println("w:"+w);
		int h = msize * modules1Side;

		// RGB info
		Yuv = new double[h][w];
		yUv = new double[h][w];
		yuV = new double[h][w];
		des = new byte[modules1Side][modules1Side];

		// RGB to YUV conversion
		double[] rgb = new double[3];
		double r, g, b;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				rgb = img.get(j, i);
				r = rgb[2];
				g = rgb[1];
				b = rgb[0];
				Yuv[i][j] = 0.299*r + 0.587*g + 0.114*b;
				yUv[i][j] = -0.1684*r - 0.3316*g + 0.5*b;
				yuV[i][j] = 0.5*r - 0.4187*g - 0.0813*b;
			}
		}

		// average pixel value at central region (minmax)
		int min = 255;
		int max = 0;
		int len_h = (h >> 1) + (h >> 2);
		int len_w = (w >> 1) + (w >> 2);
		for (int i = (h >> 2); i < len_h; i++) {
			for (int j = (w >> 2); j < len_w; j++) {
				if (min > Yuv[i][j]) {
					min = round_double2int(Yuv[i][j]);
				}
				if (max < Yuv[i][j]) {
					max = round_double2int(Yuv[i][j]);
				}
			}
		}
		Ave = (min + max) >> 1;
		// System.out.println(Ave);

		double mean1module, var1module;
		weightmod = new byte[modules1Side][modules1Side];

		for (int i = 0; i < modules1Side; i++) {
			for (int j = 0; j < modules1Side; j++) {
				mean1module = 0;
				for (int y = 0; y < msize; y++) {
					for (int x = 0; x < msize; x++) {
						mean1module += Yuv[msize*i + y][msize*j + x];
					}
				}
				mean1module /= msize*msize;

				var1module = 0;
				for (int y = 0; y < msize; y++) {
					for (int x = 0; x < msize; x++) {
						var1module += Math.pow(Yuv[msize*i + y][msize*j + x] - mean1module, 2);
					}
				}
				// variance of the module
				weightmod[i][j] = (byte) (Math.sqrt(var1module / (msize*msize)));
				if (weightmod[i][j] > 127)
					weightmod[i][j] = 127;

				// binarization
				if (mean1module > Ave)
					des[i][j] = 0;
				else
					des[i][j] = 1;
			}
		}
	}

	public static void weightingBackGround(String file) {
		// avoid foreground
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		File f = new File(file);
		Mat image = Imgcodecs.imread(f.getAbsolutePath());
		if (image == null) {
			throw new IllegalArgumentException("Illegal input file.");
		}
		Mat mask = new Mat(); // mask image
		Mat bgModel = new Mat(); // background model
		Mat fgModel = new Mat(); // foreground model
		Rect rect = new Rect(image.rows()/8, image.cols()/8, image.rows()*7/8, image.cols()*7/8); // rough classification (square)
		Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3));
		Imgproc.grabCut(image, mask, rect, bgModel, fgModel, 1, 0); // graph cut operation
		Core.compare(mask, source, mask, Core.CMP_EQ);
		Mat fg = new Mat(image.size(), CvType.CV_8UC1, new Scalar(0, 0, 0)); // foreground image
		image.copyTo(fg, mask); // extract a foreground image
		Imgcodecs.imwrite("./tmp/grabcut.png", fg); 
		Mat fg_resize = new Mat();
		Size sz = new Size(modules1Side, modules1Side);
		Imgproc.resize(fg, fg_resize, sz);

		double[] RGB = new double[3];
		
		// high weight for outer region of an input image
		for (int edge = 0; edge < 8; edge++) {
			for (int i = edge; i < modules1Side - edge; i++) {
				RGB = fg_resize.get(i,edge);
				if (RGB[0]+RGB[1]+RGB[2] == 0) {
					if (weightmod[i][edge] < (byte) ((64 - 8*edge) & 0xff)) {
						weightmod[i][edge] = (byte) ((64 - 8*edge) & 0xff);
					}
					if (weightmod[i][modules1Side - 1 - edge] < (byte) ((64 - 8*edge) & 0xff)) {
						weightmod[i][modules1Side - 1 - edge] = (byte) ((64 - 8*edge) & 0xff);
					}
				}
			}

			for (int j = edge; j < modules1Side - edge; j++) {
				RGB = fg_resize.get(edge,j);
				if (RGB[0]+RGB[1]+RGB[2] == 0) {
					//if (weightmod[edge][j] < (byte) ((64 - 8*edge) & 0xff)) {
					//	weightmod[edge][j] = (byte) ((64 - 8*edge) & 0xff);
					//}
					if (weightmod[modules1Side - 1 - edge][j] < (byte) ((64 - 8*edge) & 0xff)) {
						weightmod[modules1Side - 1 - edge][j] = (byte) ((64 - 8*edge) & 0xff);
					}
				}
			}
		}

		for (int i = modules1Side/4; i < modules1Side*3/4; i++) {
			for (int j = modules1Side/4; j < modules1Side*3/4; j++) {
				if (weightmod[i][j] > 4)
					weightmod[i][j] = 4;
			}
		}
	}

	public static void weightingFace(String file) {
		// avoiding face region
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		File f = new File(file);
		Mat image = Imgcodecs.imread(f.getAbsolutePath());
		if (image == null) {
			throw new IllegalArgumentException("Illegal input file.");
		}

		// -270 degree rotate
		Core.transpose(image, image);
		Core.flip(image, image, 1);

		// Detect faces in the image.
		File settingFile = new File("haarcascade_frontalface_default.xml");
		if (!settingFile.exists()) {
			throw new RuntimeException("No setting file.");
		}
		MatOfRect faces = new MatOfRect();
		CascadeClassifier faceDetector = new CascadeClassifier(settingFile.getAbsolutePath());
		faceDetector.detectMultiScale(image, faces);
		System.out.println(String.format("Detected %s faces.", faces.toArray().length));

		// Draw a bounding box around each face.
		for (Rect rect : faces.toArray()) {
			Imgproc.rectangle(image, new Point(rect.y, rect.x), new Point(rect.y + rect.height, rect.x + rect.width),
					new Scalar(0, 255, 0));
			int xmod = round_double2int(rect.x * modules1Side / image.width());
			if (xmod > 0)
				xmod--;
			int ymod = round_double2int(rect.y * modules1Side / image.height());
			if (ymod > 0)
				ymod--;
			int xwidth = round_double2int((rect.x + rect.width) * modules1Side / image.width());
			if (xwidth < modules1Side-1)
				xwidth++;
			int yheight = round_double2int((rect.y + rect.height) * modules1Side / image.height());
			if (yheight < modules1Side-1)
				yheight++;
			//System.out.println("(" + xmod + ", " + ymod + ")  (" + xwidth + ", " + yheight +")");
			int xmod2 = ymod;
			int ymod2 = modules1Side - xwidth;
			int xwidth2 = yheight;
			int yheight2 = modules1Side - xmod;					
			System.out.println("(" + xmod2 + ", " + ymod2 + ")  (" + xwidth2 + ", " + yheight2 +")" );
	
			for (int i = ymod2; i < yheight2; i++) {
				for (int j = xmod2; j < xwidth2; j++) {
					weightmod[j][i] = -127;
				}
			}
		}
	}

	public static int round_double2int(double x) {
		if (x < 0) {
			return 0;
		}
		if (x > 255) {
			return 255;
		}
		return (int) (x + 0.5);
	}

	public static double weight_xy(int x, int y, int Th, double var) {
		int x_center = (int) ((msize - 1) / 2);
		int y_center = (int) ((msize - 1) / 2);

		double distance = Math.pow(x - x_center, 2) + Math.pow(y - y_center, 2);
		double gaussian = Math.exp(-distance / (2*var)) * Th;

		return gaussian;
	}

	public static String getPreffix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(0, point);
		}
		return fileName;
	}

	public static void makefile(byte[][] s) {
		BufferedImage im = new BufferedImage(msize*modules1Side + 2*msize, msize*modules1Side + 2*msize, BufferedImage.TYPE_INT_RGB);
		Graphics gqr = im.getGraphics();
		gqr.setColor(Color.WHITE);
		gqr.fillRect(0, 0, msize*modules1Side + 2*msize, msize*modules1Side + 2*msize);
		// ---- image processing ----
		int R, G, B;
		int x_axis, y_axis;
		int type;
		double weight;

		for (int i = 0; i < modules1Side; i++) {
			for (int j = 0; j < modules1Side; j++) {
				for (int y = 0; y < msize; y++) {
					for (int x = 0; x < msize; x++) {
						y_axis = msize*i + y;
						x_axis = msize*j + x;
						weight = weight_xy(y, x, Th, var);
						if (weight > 4)
							type = 1;
						else
							type = 0;

						if (s[i][j] == 2) {
							if (Yuv[y_axis][x_axis] > Ave - Th) {
								Yuv[y_axis][x_axis] = Ave - Th;
							}
						} else {
							if (type == 1) {
								if (s[i][j] == 1 && Yuv[y_axis][x_axis] > Ave - weight) {
									Yuv[y_axis][x_axis] = Ave - weight;
								} else if (s[i][j] == 0 && Yuv[y_axis][x_axis] < Ave + weight) {
									Yuv[y_axis][x_axis] = Ave + weight;
								}
							}
						}

						// YUV to RGB conversion
						R = round_double2int(Yuv[y_axis][x_axis] + 1.4020*yuV[y_axis][x_axis]);
						G = round_double2int(Yuv[y_axis][x_axis] - 0.3441*yUv[y_axis][x_axis] - 0.7139*yuV[y_axis][x_axis]);
						B = round_double2int(Yuv[y_axis][x_axis] + 1.7718*yUv[y_axis][x_axis] - 0.0012*yuV[y_axis][x_axis]);

						Color c = new Color(R, G, B);
						gqr.setColor(c);
						gqr.fillRect(i*msize + y + msize, j*msize + x + msize, 1, 1);
					}
				}
			}
		}

		gqr.dispose();
		try {
			FilePath = "./tmp/QRcode.png";
			//System.err.println(FilePath);
			File f = new File(FilePath);
			ImageIO.write(im, "PNG", f);
		} catch (Exception ex) {
		}
	}

	public static void trimming(String file) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		File f = new File(file);
		Mat image = Imgcodecs.imread(f.getAbsolutePath());
		if (image == null) {
			throw new IllegalArgumentException("Illegal input file.");
		}

		ImageIcon icon = new ImageIcon(file);
		int iconWidth = icon.getIconWidth();
		int iconHeight = icon.getIconHeight(); 
		original_width = iconWidth;
		original_height = iconHeight;
		if (iconHeight > iconWidth)
			side1 = (int)(iconWidth * scale);
		else
			side1 = (int)(iconHeight * scale);
		msize = (int)(side1/modules1Side);
		if (side1 > original_height - 2*msize){
			side1 = original_height - 2*msize;
		}
		if (side1 > original_width - 2*msize){
			side1 = original_width - 2*msize;
		}
		
		msize = (int)(side1/modules1Side);
		side1clone = msize*modules1Side;
		clip_y = (int)(y_position*original_height) - side1clone/2;
		clip_x = (int)(x_position*original_width) - side1clone/2;
		if (clip_y < msize)
			clip_y = msize;
		if (clip_x < msize)
			clip_x = msize;
		if (clip_y + side1clone > original_height - 2*msize){
			clip_y = original_height-side1clone - msize;
		}
		if (clip_x + side1clone > original_width - 2*msize){
			clip_x = original_width-side1clone - msize;
		}
		
		System.out.println("side1:"+side1 + "  T:"+side1clone + "  gamma:"+msize);
		System.out.println("("+clip_y + ", "+clip_x+")");

		var = var + var*(msize - 3)/4.0;
		var = var*var;
		System.out.println(msize + "*" + msize + "pixels, Th = " + Th + ", var = " + var);

		Mat image_clone = image.clone();
		Rect roi = new Rect(clip_x, clip_y, side1clone, side1clone);
		try{
			Mat image2 = new Mat(image_clone, roi);
			// 270 degree rotate
			Core.transpose(image2, image2);
			Core.flip(image2, image2, 0);
			Imgcodecs.imwrite("./tmp/trimming.png", image2);
		}  catch (Exception ex) {
		}
	}

	public static void insert(String file, String file2) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		File f = new File(file);
		Mat image = Imgcodecs.imread(f.getAbsolutePath());
		if (image == null) {
			throw new IllegalArgumentException("Illegal input file.");
		}
		// -270 degree rotate
		Core.transpose(image, image);
		Core.flip(image, image, 1);

		File f2 = new File(file2);
		Mat image2 = Imgcodecs.imread(f2.getAbsolutePath());
		if (image2 == null) {
			throw new IllegalArgumentException("Illegal input file.");
		}
		Mat image2_clone = image2.clone();

		int iconHeight = msize*modules1Side + 2*msize;
		int iconWidth = msize*modules1Side + 2*msize;
		//System.out.println("iconHeight = " +iconHeight+"  iconWidth = " +iconWidth);

		double[][][] data_tr = new double[iconHeight][iconWidth][3];
		double[] tmp = new double[3];
		for (int i = 0; i < iconHeight; i++) {
			for (int j = 0; j < iconWidth; j++) {
				tmp = image.get(i, j);
				if (i < msize || i >= iconHeight-msize || j < msize || j >= iconWidth-msize){
					tmp = image2.get(i+clip_y-msize, j+clip_x-msize);
					double Y = 0.299*tmp[2] + 0.587*tmp[1] + 0.114*tmp[0];
					double U = -0.1684*tmp[2] - 0.3316*tmp[1] + 0.5*tmp[0];
					double V = 0.5*tmp[2] - 0.4187*tmp[1] - 0.0813*tmp[0];
					double weight = weight_xy(i%msize, j%msize, Th, var);
					if (Y < Ave+weight && weight > 4){
						Y = Ave+weight;
						tmp[2] = round_double2int(Y + 1.4020 * V);
						tmp[1] = round_double2int(Y - 0.3441 * U - 0.7139 * V);
						tmp[0] = round_double2int(Y + 1.7718 * U - 0.0012 * V);
					}
				}
				if ((i < msize && (j > 9*msize && j < iconWidth-9*msize))
						|| (i >= iconHeight-msize && (j < iconWidth-9*msize))
						|| (j < msize && i>9*msize)
						|| (j >= iconWidth-msize && (i > 9*msize && i < iconHeight-9*msize))){
					tmp = image2.get(i+clip_y-msize, j+clip_x-msize);
				}
						
				data_tr[i][j][0] = tmp[0];
				data_tr[i][j][1] = tmp[1];
				data_tr[i][j][2] = tmp[2];
			}
		}
		//System.out.println("originalHeight = " +original_height + "  originalWidth = " +original_width);

		for (int i = clip_y; i < clip_y+iconHeight; i++) {
			for (int j = clip_x; j < clip_x+iconWidth; j++) {
				image2_clone.put(i-msize, j-msize, data_tr[i-clip_y][j-clip_x]);
			}
		}		
		
		String ecc = "H";
		if (el == 0) {
			ecc = "L";
		} else if (el == 1) {
			ecc = "M";
		} else if (el == 2) {
			ecc = "Q";
		}

		Imgcodecs.imwrite("./QRfiles/" + getPreffix(f2.getName()) + "_" + version + ecc + "_" + msize + "x" + msize + "_" + scale +".png", image2_clone);
		System.out.println("QR code is output successfully.");

	}
}
