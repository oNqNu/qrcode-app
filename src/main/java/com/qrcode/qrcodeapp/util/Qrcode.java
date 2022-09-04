package com.qrcode.qrcodeapp.util;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Arrays;

public class Qrcode{

	static final String QRCODE_DATA_PATH = "qrcode_data";
	public String FilePath = null;
	double imgW, imgH, desH, desW;
	int[][] imar, imag, imab, rgb;
	char qrcodeErrorCorrect;
	char qrcodeEncodeMode;
	int mod;
	int qrcodeVersion;
	int kaisu = 100;
	int qrcodeStructureappendN;
	int qrcodeStructureappendM;
	int qrcodeStructureappendParity;
	static int codewordsCounterda;
	byte maskNumber = 0;
	byte maskContent;

	static final long serialVersionUID = 1;
	String qrcodeStructureappendOriginaldata;

	public int getQrcodeVersion() {
		return qrcodeVersion;
	}

	public void setQrcodeVersion(int ver) {
		if (ver >= 0 && ver <= 40) {
			qrcodeVersion = ver;
		}
	}

	public char getQrcodeErrorCorrect() {
		return qrcodeErrorCorrect;
	}

	public void setQrcodeErrorCorrect(int ecc) {
		if(ecc == 0) qrcodeErrorCorrect = 'L';
		else if(ecc == 1) qrcodeErrorCorrect = 'M';
		else if(ecc == 2) qrcodeErrorCorrect = 'Q';
		else qrcodeErrorCorrect = 'H';
	}

	public byte getQrcodemask() {
		return maskNumber;
	}

	public void setQrcodemask(byte mask) {
		maskNumber = mask;
		maskContent = (byte) (1 << maskNumber);
	}

	public int getQrcodekaisu() {
		return kaisu;
	}

	public void setQrcodekaisu(int kai) {
		kaisu = kai;
	}

	public char getQrcodeEncodeMode() {
		return qrcodeEncodeMode;
	}

	public void setQrcodeEncodeMode(int encMode) {
		if(encMode == 0) qrcodeEncodeMode = '8';
		else if(encMode == 1) qrcodeEncodeMode = 'A';
		else qrcodeEncodeMode = 'N';
	}

	public void setQrcodeMode(int mode) {
		mod = mode;
	}

	public int calStructureappendParity(byte[] originaldata) {
		int originaldataLength;
		int i = 0;
		int structureappendParity = 0;

		originaldataLength = originaldata.length;
		if (originaldataLength > 1) {
			structureappendParity = 0;
			while (i < originaldataLength) {
				structureappendParity = (structureappendParity ^ (originaldata[i] & 0xFF));
				i++;
			}
		} else {
			structureappendParity = -1;
		}
		return structureappendParity;
	}

	public Qrcode() {
		qrcodeErrorCorrect = 'L';
		qrcodeEncodeMode = '8';
		qrcodeVersion = 0;
		qrcodeStructureappendN = 0;
		qrcodeStructureappendM = 0;
		qrcodeStructureappendParity = 0;
		qrcodeStructureappendOriginaldata = "";
		mod = 0;
		kaisu = 100;
		maskNumber = 0;
		maskContent = (byte) (1 << maskNumber);
	}

	/* ---- 格納データ ---- */
	public byte[][] calQrcode(byte[] qrcodeData) {
		int dataLength;
		int dataCounter = 0;

		dataLength = qrcodeData.length;
		int[] dataValue = new int[dataLength + 32];
		byte[] dataBits = new byte[dataLength + 32];

		if (dataLength <= 0) {
			byte ret[][] = { { 0 } };
			return ret;
		}
		if (qrcodeStructureappendN > 1) {
			dataValue[0] = 3;
			dataBits[0] = 4;

			dataValue[1] = qrcodeStructureappendM - 1;
			dataBits[1] = 4;

			dataValue[2] = qrcodeStructureappendN - 1;
			dataBits[2] = 4;

			dataValue[3] = qrcodeStructureappendParity;
			dataBits[3] = 8;

			dataCounter = 4;
		}
		dataBits[dataCounter] = 4;

		/* --- エンコードモード選択 --- */
		int[] codewordNumPlus;
		int codewordNumCounterValue;
		switch (qrcodeEncodeMode) {

		/* ---- 英数字モード --- */
		case 'A':
			codewordNumPlus = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2,
					2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4,
					4, 4, 4, 4, 4, 4, 4, 4, 4 };
			dataValue[dataCounter] = 2;
			dataCounter++;
			dataValue[dataCounter] = dataLength;
			dataBits[dataCounter] = 9;
			codewordNumCounterValue = dataCounter;
			dataCounter++;
			for (int i = 0; i < dataLength; i++) {
				char chr = (char) qrcodeData[i];
				byte chrValue = 0;
				if (chr >= 48 && chr < 58) {
					chrValue = (byte) (chr - 48);
				} else {
					if (chr >= 65 && chr < 91) {
						chrValue = (byte) (chr - 55);
					} else {
						if (chr == 32) {
							chrValue = 36;
						}
						if (chr == 36) {
							chrValue = 37;
						}
						if (chr == 37) {
							chrValue = 38;
						}
						if (chr == 42) {
							chrValue = 39;
						}
						if (chr == 43) {
							chrValue = 40;
						}
						if (chr == 45) {
							chrValue = 41;
						}
						if (chr == 46) {
							chrValue = 42;
						}
						if (chr == 47) {
							chrValue = 43;
						}
						if (chr == 58) {
							chrValue = 44;
						}
					}
				}
				if ((i % 2) == 0) {
					dataValue[dataCounter] = chrValue;
					dataBits[dataCounter] = 6;
				} else {
					dataValue[dataCounter] = dataValue[dataCounter] * 45
							+ chrValue;
					dataBits[dataCounter] = 11;
					if (i < dataLength - 1) {
						dataCounter++;
					}
				}
			}
			dataCounter++;
			break;

		/* ---- 数字モード ---- */
		case 'N':
			codewordNumPlus = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2,
					2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4,
					4, 4, 4, 4, 4, 4, 4, 4, 4 };
			// 数字モード0001
			dataValue[dataCounter] = 1;
			// データ長
			dataCounter++;
			dataValue[dataCounter] = dataLength;
			// 10ビットで表記
			dataBits[dataCounter] = 10; /* #version 1-9 */
			codewordNumCounterValue = dataCounter;
			dataCounter++;
			for (int i = 0; i < dataLength; i++) {
				if ((i % 3) == 0) {
					dataValue[dataCounter] = (int) (qrcodeData[i] - 0x30);
					dataBits[dataCounter] = 4;
				} else {
					dataValue[dataCounter] = dataValue[dataCounter] * 10
							+ (int) (qrcodeData[i] - 0x30);
					if ((i % 3) == 1) {
						dataBits[dataCounter] = 7;
					} else {
						dataBits[dataCounter] = 10;
						if (i < dataLength - 1) {
							dataCounter++;
						}
					}
				}
			}
			dataCounter++;
			break;

		/* ---- 8ビットバイトモード ---- */
		default:
			codewordNumPlus = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8,
					8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
					8, 8, 8, 8, 8, 8, 8, 8, 8 };
			dataValue[dataCounter] = 4;
			dataCounter++;
			dataValue[dataCounter] = dataLength;
			dataBits[dataCounter] = 8; /* #version 1-9 */
			codewordNumCounterValue = dataCounter;
			dataCounter++;
			for (int i = 0; i < dataLength; i++) {
				dataValue[i + dataCounter] = (qrcodeData[i] & 0xFF);
				dataBits[i + dataCounter] = 8;
			}
			dataCounter += dataLength;
			break;
		}

		int totalDataBits = 0;
		for (int i = 0; i < dataCounter; i++) {
			totalDataBits += dataBits[i];
		}

		/* ---- 誤り訂正レベルの選択 ---- */
		int ec;
		switch (qrcodeErrorCorrect) {
		case 'L':
			ec = 1;
			break;
		case 'Q':
			ec = 3;
			break;
		case 'H':
			ec = 2;
			break;
		default:
			ec = 0;
		}

		int[][] maxDataBitsArray = {
				{ 0, 128, 224, 352, 512, 688, 864, 992, 1232, 1456, 1728, 2032,
						2320, 2672, 2920, 3320, 3624, 4056, 4504, 5016, 5352,
						5712, 6256, 6880, 7312, 8000, 8496, 9024, 9544, 10136,
						10984, 11640, 12328, 13048, 13800, 14496, 15312, 15936,
						16816, 17728, 18672 },

				{ 0, 152, 272, 440, 640, 864, 1088, 1248, 1552, 1856, 2192,
						2592, 2960, 3424, 3688, 4184, 4712, 5176, 5768, 6360,
						6888, 7456, 8048, 8752, 9392, 10208, 10960, 11744,
						12248, 13048, 13880, 14744, 15640, 16568, 17528, 18448,
						19472, 20528, 21616, 22496, 23648 },

				{ 0, 72, 128, 208, 288, 368, 480, 528, 688, 800, 976, 1120,
						1264, 1440, 1576, 1784, 2024, 2264, 2504, 2728, 3080,
						3248, 3536, 3712, 4112, 4304, 4768, 5024, 5288, 5608,
						5960, 6344, 6760, 7208, 7688, 7888, 8432, 8768, 9136,
						9776, 10208 },

				{ 0, 104, 176, 272, 384, 496, 608, 704, 880, 1056, 1232, 1440,
						1648, 1952, 2088, 2360, 2600, 2936, 3176, 3560, 3880,
						4096, 4544, 4912, 5312, 5744, 6032, 6464, 6968, 7288,
						7880, 8264, 8920, 9368, 9848, 10288, 10832, 11408,
						12016, 12656, 13328 } };

		int maxDataBits = 0;

		if (qrcodeVersion == 0) {
			/* 自動バージョン決め */

			qrcodeVersion = 1;
			for (int i = 1; i <= 40; i++) {
				if ((maxDataBitsArray[ec][i]) >= totalDataBits
						+ codewordNumPlus[qrcodeVersion]) {
					maxDataBits = maxDataBitsArray[ec][i];
					break;
				}
				qrcodeVersion++;
			}
		} else {
			maxDataBits = maxDataBitsArray[ec][qrcodeVersion];
		}
		totalDataBits += codewordNumPlus[qrcodeVersion];
		dataBits[codewordNumCounterValue] += codewordNumPlus[qrcodeVersion];

		int[] maxCodewordsArray = { 0, 26, 44, 70, 100, 134, 172, 196, 242,
				292, 346, 404, 466, 532, 581, 655, 733, 815, 901, 991, 1085,
				1156, 1258, 1364, 1474, 1588, 1706, 1828, 1921, 2051, 2185,
				2323, 2465, 2611, 2761, 2876, 3034, 3196, 3362, 3532, 3706 };

		int maxCodewords = maxCodewordsArray[qrcodeVersion];

		int[] matrixRemainBit = { 0, 0, 7, 7, 7, 7, 7, 0, 0, 0, 0, 0, 0, 0, 3,
				3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 0,
				0, 0, 0, 0, 0 };

		/* ---- read version ECC data file */

		int byte_num = matrixRemainBit[qrcodeVersion] + (maxCodewords << 3);

		byte[] matrixX = new byte[byte_num];
		byte[] matrixY = new byte[byte_num];
		byte[] maskArray = new byte[byte_num];
		byte[] formatInformationX2 = new byte[15];
		byte[] formatInformationY2 = new byte[15];
		byte[] rsEccCodewords = new byte[1];
		byte[] rsBlockOrderTemp = new byte[128];

		try {
			String filename = QRCODE_DATA_PATH + "/qrv"
					+ Integer.toString(qrcodeVersion) + "_"
					+ Integer.toString(ec) + ".dat";

			InputStream fis = Qrcode.class.getResourceAsStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(matrixX);
			bis.read(matrixY);
			bis.read(maskArray);
			bis.read(formatInformationX2);
			bis.read(formatInformationY2);
			bis.read(rsEccCodewords);
			bis.read(rsBlockOrderTemp);
			bis.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte rsBlockOrderLength = 1;
		for (byte i = 1; i < 128; i++) {
			if (rsBlockOrderTemp[i] == 0) {
				rsBlockOrderLength = i;
				break;
			}
		}

		byte[] rsBlockOrder = new byte[rsBlockOrderLength];

		System.arraycopy(rsBlockOrderTemp, 0, rsBlockOrder, 0,
				rsBlockOrderLength);

		byte[] formatInformationX1 = { 0, 1, 2, 3, 4, 5, 7, 8, 8, 8, 8, 8, 8, 8, 8 };
		byte[] formatInformationY1 = { 8, 8, 8, 8, 8, 8, 8, 8, 7, 5, 4, 3, 2, 1, 0 };

		int maxDataCodewords = maxDataBits >> 3;

		/* -- read frame data -- */
		int modules1Side = 4 * qrcodeVersion + 17;

		int matrixTotalBits = modules1Side * modules1Side;
		byte[] frameData = new byte[matrixTotalBits + modules1Side];
		try {
			String filename = QRCODE_DATA_PATH + "/qrvfr"
					+ Integer.toString(qrcodeVersion) + ".dat";
			InputStream fis = Qrcode.class.getResourceAsStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(frameData);
			bis.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* --- 終端コード語生成 */
		if (totalDataBits <= maxDataBits - 4) {
			dataValue[dataCounter] = 0;
			dataBits[dataCounter] = 4;
		} else {
			if (totalDataBits < maxDataBits) {
				dataValue[dataCounter] = 0;
				dataBits[dataCounter] = (byte) (maxDataBits - totalDataBits);
			} else {
				if (totalDataBits > maxDataBits) {
					System.err.println("型式がデータ容量を超えています！");
					return null;
				}
			}
		}
		
		// ここから記述しています
		/* ---- マトリックス生成・初期化 */
		byte[][] matrixContent = new byte[modules1Side][modules1Side];
		for (int i = 0; i < modules1Side; i++) {
			for (int j = 0; j < modules1Side; j++) {
				matrixContent[i][j] = 0;
			}
		}

		// 2値化デザインデータ01→マスク情報8bit→01
		byte[][] desxor = new byte[modules1Side][modules1Side];
		for (int i = 0; i < maxCodewords; i++) {
			for (int j = 7; j >= 0; j--) {
				int codewordBitsNumber = (i * 8) + j;
				desxor[matrixX[codewordBitsNumber] & 0xFF][matrixY[codewordBitsNumber] & 0xFF] = (byte) ((255 * QRmain.des[matrixX[codewordBitsNumber] & 0xFF][matrixY[codewordBitsNumber] & 0xFF]) ^ maskArray[codewordBitsNumber]);
				if ((desxor[matrixX[codewordBitsNumber] & 0xFF][matrixY[codewordBitsNumber] & 0xFF] & maskContent) != 0) {
					desxor[matrixX[codewordBitsNumber] & 0xFF][matrixY[codewordBitsNumber] & 0xFF] = 1;
				} else {
					desxor[matrixX[codewordBitsNumber] & 0xFF][matrixY[codewordBitsNumber] & 0xFF] = 0;
				}
			}
		}

		// 重み計算
		byte[][] weight = new byte[modules1Side][modules1Side];	
		for (int i = 0; i < modules1Side; i++){
			for (int j = 0; j < modules1Side; j++){
				weight[i][j] = QRmain.weightmod[i][j];
			}
		}
		
		int code[] = new int [rsBlockOrder.length];
		int data[] = new int [rsBlockOrder.length];
		for (int t = 0; t < rsBlockOrderLength; t++) {
			code[t] = rsBlockOrder[t] & 0xFF;
			data[t] = code[t] - rsEccCodewords[0];
		}

		// XOR演算した2値化デザインデータ(0,1)からコード語を生成
		byte[] datarow = divideDataBy8Bits(dataValue, dataBits,
				maxDataCodewords, maxCodewords, matrixContent, matrixX,
				matrixY, desxor);
		int [] weightrow = divideweightBy8Bits(dataValue, dataBits,
				maxDataCodewords, maxCodewords, matrixContent, matrixX,
				matrixY, weight);
		int datacount = codewordsCounterda + 1;

		byte[] memorycode = new byte[maxCodewords];
		byte[] datarow2 = new byte[maxDataCodewords];

		System.arraycopy(datarow, 0, datarow2, 0, maxDataCodewords);

		switch (mod) {
		// 消失訂正ありの場合
		case 0:
			if (rsBlockOrder.length == 1) {
				// データ列を企画に基づいてRS符号化
				int mindes = modules1Side * modules1Side;
				int count = 0;
				int[] r = new int[maxCodewords - maxDataCodewords];
				boolean flag = true;

				for (int N = 0; N < kaisu; N++) {
					flag = true;
					
					EncodeProp(weightrow, r, maxCodewords-maxDataCodewords, maxCodewords, datacount, N);

					// 固定したシンボルからtestRSを生成
					byte[] testRS = NonSystematicEncoding(maxCodewords,
							maxDataCodewords, datarow, datacount, r);
					if (testRS == null) {
						flag = false;
						N--;
					}
					if (flag) {
						count = 0;
						for (int m = 0; m < maxCodewords; m++) {
							byte xorcode = (byte) (datarow[m] ^ testRS[m]);
							for (int j = 7; j >= 0; j--) {
								count += (byte) (xorcode & 1);
								xorcode = (byte) ((xorcode & 0xFF) >>> 1);
							}
						}
						// 最良のパターン更新部
						if (mindes > count) {
							mindes = count;
							System.arraycopy(testRS, 0, memorycode, 0, maxCodewords);
						}
					}
				}
				 System.out.println("#error: " + mindes);
			}

			// 複数のRSブロックを持つ場合
			else {
				int sum =0;
				int n[] = new int [rsBlockOrder.length];
				int k[] = new int [rsBlockOrder.length];
				int m = rsEccCodewords[0];
				for (int t = 0; t < rsBlockOrderLength; t++) {
					n[t] = rsBlockOrder[t] & 0xFF;
					k[t] = n[t] - m;
				}
				int mindes = 0;
				int count = 0;
				int rsBlockOrdermax = 0;

				// RSブロックの中で総コード語が最大のものを探す
				rsBlockOrdermax = n[rsBlockOrderLength - 1];
				byte rs[][] = new byte[rsBlockOrderLength][rsBlockOrdermax];
				byte RSblock[][] = new byte[rsBlockOrderLength][rsBlockOrdermax];
				byte RSdata[][] = new byte[rsBlockOrderLength][rsBlockOrdermax- m];
				byte RSecc[][] = new byte[rsBlockOrderLength][m];
				int rs_weight[][] = new int[rsBlockOrderLength][rsBlockOrdermax];
				int RSdata_weight[][] = new int[rsBlockOrderLength][rsBlockOrdermax- m];
				int RSecc_weight[][] = new int[rsBlockOrderLength][m];

				int l = 0;
				for (int t = 0; t < rsBlockOrderLength; t++) {
					System.arraycopy(datarow, l, RSdata[t], 0, k[t]);
					System.arraycopy(datarow, maxDataCodewords + t * m, RSecc[t], 0, m);
					System.arraycopy(weightrow, l, RSdata_weight[t], 0, k[t]);
					System.arraycopy(weightrow, maxDataCodewords + t * m, RSecc_weight[t], 0, m);
				    l += k[t];
				}

				for (int t = 0; t < rsBlockOrderLength; t++) {
					System.arraycopy(RSdata[t], 0, rs[t], 0, k[t]);
					System.arraycopy(RSecc[t], 0, rs[t], k[t], m);
					System.arraycopy(RSdata_weight[t], 0, rs_weight[t], 0, k[t]);
					System.arraycopy(RSecc_weight[t], 0, rs_weight[t], k[t], m);
				}
						
				for (int t = 0; t < rsBlockOrderLength; t++) {
					mindes = modules1Side * modules1Side;
					int[] r = new int[m]; //検査点
					boolean flag = true;
					int start;
					if (datacount >= k[t])
						start = k[t];
					else if (datacount < 0)
						start = 0;
					else
						start = datacount;
					//System.out.println("start=" + start + " n=" + n[t] + " k=" + k[t] + " m=" + m);
										
					// Nは消失訂正の試行回数
					for (int N = 0; N < kaisu; N++) {
						flag = true;
												
						// 提案方式
						EncodeProp(rs_weight[t], r, m, n[t], start, N);
						
						// 固定したシンボルからtestRSを生成
						byte[] testRS = NonSystematicEncoding(n[t], k[t], rs[t], datacount, r);
						if (testRS == null) {
							flag = false;
							N--;
						}
						if (flag) {
							// 誤差countの計測
							count = 0;
							for (int i = 0; i < n[t]; i++) {
								byte xorcode = (byte) (rs[t][i] ^ testRS[i]);
								for (int j = 7; j >= 0; j--) {
									count += (byte) (xorcode & 1);
									xorcode = (byte) ((xorcode & 0xFF) >>> 1);
								}
							}

							// 最良のパターン更新部
							if (mindes > count) {
								mindes = count;
								System.arraycopy(testRS, 0, RSblock[t], 0, n[t]);
							}
						}
					} // N回のループ終了
					sum += mindes;
					System.arraycopy(RSblock[t], 0, RSdata[t], 0, k[t]);
					System.arraycopy(RSblock[t], k[t], RSecc[t], 0, m);
					datacount -= k[t];
				}// tループ終了
				l = 0;
				for (int t = 0; t < rsBlockOrderLength; t++) {
					System.arraycopy(RSdata[t], 0, memorycode, l, k[t]);
					System.arraycopy(RSecc[t], 0, memorycode, maxDataCodewords + t * m, m);
				    l += k[t];
				}
				 System.out.println("誤差 " + sum);
				 //System.out.println(Arrays.toString(memorycode));
			}
			break;

		// 消失訂正なしの場合
		default:
			byte[] RS = calculateRSECC3(datarow2, rsEccCodewords[0],
					rsBlockOrder, maxDataCodewords, maxCodewords);
			int count = 0;
			for (int s = 0; s < maxCodewords; s++) {
				byte xorcode = (byte) (datarow[s] ^ RS[s]);
				for (int j = 7; j >= 0; j--) {
					count += (byte) (xorcode & 1);
					xorcode = (byte) ((xorcode & 0xFF) >>> 1);
				}
			}
			for (int i = 0; i < maxCodewords; i++) {
				memorycode[i] = RS[i];
			}
			System.out.println("誤差 " + count);
			System.arraycopy(RS, 0, memorycode, 0, maxCodewords);
			break;
		}

		// 最良の符号をマトリックス上に配置
		for (int i = 0; i < maxCodewords; i++) {
			byte codeword_i = memorycode[i];
			for (int j = 7; j >= 0; j--) {
				int codewordBitsNumber = (i * 8) + j;
				matrixContent[matrixX[codewordBitsNumber] & 0xFF][matrixY[codewordBitsNumber] & 0xFF] = (byte) ((255 * (codeword_i & 1)) ^ maskArray[codewordBitsNumber]);
				codeword_i = (byte) ((codeword_i & 0xFF) >>> 1);
			}
		}

		// 残余ビットの配置
		for (int matrixRemain = matrixRemainBit[qrcodeVersion]; matrixRemain > 0; matrixRemain--) {
			int remainBitTemp = matrixRemain + (maxCodewords * 8) - 1;
			matrixContent[matrixX[remainBitTemp] & 0xFF][matrixY[remainBitTemp] & 0xFF] = (byte) (255 ^ maskArray[remainBitTemp]);
		}

		/* --- フォーマット情報 --- */
		byte formatInformationValue = (byte) (ec << 3 | maskNumber);
		String[] formatInformationArray = { "101010000010010",
				"101000100100101", "101111001111100", "101101101001011",
				"100010111111001", "100000011001110", "100111110010111",
				"100101010100000", "111011111000100", "111001011110011",
				"111110110101010", "111100010011101", "110011000101111",
				"110001100011000", "110110001000001", "110100101110110",
				"001011010001001", "001001110111110", "001110011100111",
				"001100111010000", "000011101100010", "000001001010101",
				"000110100001100", "000100000111011", "011010101011111",
				"011000001101000", "011111100110001", "011101000000110",
				"010010010110100", "010000110000011", "010111011011010",
				"010101111101101" };

		for (int i = 0; i < 15; i++) {
			matrixContent[formatInformationX1[i] & 0xFF][formatInformationY1[i] & 0xFF] = 0;
			matrixContent[formatInformationX2[i] & 0xFF][formatInformationY2[i] & 0xFF] = 0;
		}

		for (int i = 0; i < 15; i++) {
			byte content = Byte
					.parseByte(formatInformationArray[formatInformationValue]
							.substring(i, i + 1));
			matrixContent[formatInformationX1[i] & 0xFF][formatInformationY1[i] & 0xFF] = (byte) (content * 255);
			matrixContent[formatInformationX2[i] & 0xFF][formatInformationY2[i] & 0xFF] = (byte) (content * 255);
		}

		// QRコードのデータ出力部
		byte[][] out = new byte[modules1Side][modules1Side];
		int c = 0;
		
		for (int i = 0; i < modules1Side; i++) {
			for (int j = 0; j < modules1Side; j++) {
				if ( frameData[c] == (char) 49) {
					if ((i < 8 && j < 8) || (i < 8 && j > modules1Side-9) || (i > modules1Side-9 && j < 8) || (i > modules1Side-10 && i < modules1Side-4 && j > modules1Side-10 && j < modules1Side-4)) {
						out[i][j] = 2;
					}
					else {
						out[i][j] = 1;
					}
				}
				else if	((matrixContent[i][j] & maskContent) != 0) {
					out[i][j] = 1;
				}
				else {
					out[i][j] = 0;
				}
				c++;
			}
			c++;
		}
		return out;
	}

	// encoding each RS codeword
	private static void EncodeProp(int[] wcode, int[] r, int m, int n, int start, int N){

		Sfmt s = new Sfmt((int)N);  // Nの値を乱数の種とする
		//Sfmt s = new Sfmt((int) System.currentTimeMillis());  // 時間により乱数が変化
		int max ;
		int idx = 0;
		int x;
		int b;
		int[] idx_bak = new int[m];
		int[] wcode_bak = new int[m];
		int[] wcode_sort = new int[n-start];
	 
		for(x=start; x<n; x++){
			wcode_sort[x-start]=wcode[x];
		}

		// 重み上位bビット目を求める
		Arrays.sort(wcode_sort);
		b = 1;
		idx = n - start - m - b;
		if (idx < 0)
			idx = 0;
		max = wcode_sort[idx];
		//System.out.println("max=" + max);
				
		for(int i=0; i<m; i++ ){
			int flag = 0;
			while(flag==0){
				for(x=start ;x<n ;x++){
					if(wcode[x] >= max && s.NextInt(16)==0){
						idx = x;
						flag = 1;
					}
				}
			}
			
			idx_bak[i] = idx;
			wcode_bak[i] = wcode[idx];
			r[i] = idx;  //検査点を設定
			wcode[idx] = -1;  //2度検査点に選ばれないように一度選んだコード語は重み0とする
		 }	
		
		for(int i=0; i<m; i++){
			wcode[r[i]] = wcode_bak[i];
			r[i] = idx_bak[i];
		}
	}
	
	// データを8ビットに分割しコード語を生成
	private static byte[] divideDataBy8Bits(int[] dataValue, byte[] dataBits,
			int maxDataCodewords, int maxCodewords, byte[][] matrix, byte[] X,
			byte[] Y, byte[][] DES) {

		int codewordsCounter = 0;
		int buffer;
		byte[] codewords = new byte[maxCodewords];

		for (int i = 0; i < maxCodewords; i++) {
			codewords[i] = 0;
		}

		int loop = 0;
		for (int i = 0; i < maxCodewords; i++) {
			for (int j = 7; j >= 0; j--) {
				int codewordBitsNumber = (i * 8) + j;
				buffer = DES[X[codewordBitsNumber] & 0xFF][Y[codewordBitsNumber] & 0xFF];
				if (codewordsCounter >= maxCodewords) {
					break;
				}
				codewords[codewordsCounter] += buffer << loop;
				if (loop == 7) {
					codewordsCounter++;
					loop = -1;
				}
				loop++;
			}
		}

		// 格納データ部分を上書きする
		int l1 = dataBits.length;
		int remainingBits = 8;
		int bufferda;
		int bufferBitsda;
		boolean flag;
		boolean flag2;
		if (l1 != dataValue.length) {
			System.err.println("l1 != dataValue.length");
		}

		flag2 = true;
		codewordsCounterda = 0;

		if (flag2) {
			for (int i = 0; i < l1; i++) {
				bufferda = dataValue[i];
				bufferBitsda = dataBits[i];
				flag = true;
				if (bufferBitsda == 0) {
					flag2 = false;
					break;
				}
				while (flag) {
					if (remainingBits > bufferBitsda) {
						codewords[codewordsCounterda] = (byte) ((codewords[codewordsCounterda] << bufferBitsda) | bufferda);
						remainingBits -= bufferBitsda;
						flag = false;
					} else {
						bufferBitsda -= remainingBits;
						codewords[codewordsCounterda] = (byte) ((codewords[codewordsCounterda] << remainingBits) | (bufferda >> bufferBitsda));
						if (bufferBitsda == 0) {
							flag = false;
						} else {
							bufferda = (bufferda & ((1 << bufferBitsda) - 1));
							flag = true;
						}
						codewordsCounterda++;
						remainingBits = 8;
					}
				}
			}
			if (remainingBits != 8) {
				codewords[codewordsCounterda] = (byte) (codewords[codewordsCounterda] << remainingBits);
			} else {
				codewordsCounterda--;
			}
		}
		return codewords;
	}

	// 重みデータを8ビット分に分割し8モジュール当たりの重みを生成
	private static int[] divideweightBy8Bits(int[] dataValue, byte[] dataBits,
			int maxDataCodewords, int maxCodewords, byte[][] matrix, byte[] X,
			byte[] Y, byte[][] weight) {

		int codewordsCounter = 0;
		int buffer;
		int[] weight8module = new int[maxCodewords];

		for (int i = 0; i < maxCodewords; i++) {
			weight8module[i] = 1016;   // initial value is 127*8
		}

		int loop = 0;
		for (int i = 0; i < maxCodewords; i++) {
			for (int j = 7; j >= 0; j--) {
				int codewordBitsNumber = (i * 8) + j;
				buffer = weight[X[codewordBitsNumber] & 0xFF][Y[codewordBitsNumber] & 0xFF];
				if (codewordsCounter >= maxCodewords) {
					break;
				}
				weight8module[codewordsCounter] += buffer;
				if (loop == 7) {
					codewordsCounter++;
					loop = -1;
				}
				loop++;
			}
		}

		return weight8module;
	}

	private static byte[] NonSystematicEncoding(int n, int k, byte[] codewords, int datalength, int[] r) {
		// 検査点数
		int m = n - k;
		// rを昇順に並べる
		Arrays.sort(r);
		// 各パラメータのチェック
		if (m < 0) {
			System.err.println("情報点数が符号長を超えています");
			return null;
		}
		if (m != r.length) {
			System.err.println("rの長さ" + r.length + "が検査点数" + m + "と一致しません");
			return null;
		}
		for (int i = 0; i < r.length - 1; i++) {
			if (r[i] == r[i + 1]) {
				System.err.println("rに重複があります");
				return null;
			}
		}

		// 各値の定義
		// RS符号
		byte[] c = new byte[n];
		// 消失部
		byte[] cp = new byte[m];
		// 検査行列
		int[][] h = new int[n][m];
		boolean flag = true;
		// 有限体を導入
		Galois g = Galois.getInstance();

		// cの初期化
		for (int a = 0; a < n; a++) {
			c[a] = 0;
		}

		// ctcの初期化
		for (int a = 0; a < m; a++) {
			cp[a] = 0;
		}

		// 検査行列の生成
		for (int j = 0; j < m; j++) {
			for (int i = 0; i < n; i++) {
				h[i][j] = ((n - i - 1) * j) % 255;
			}
		}

		// 検査行列の対角化を行う
		// hの対角成分を255にし，各行も変化させる
		for (int j = 0; j < r.length; j++) {
			// diff:255と対角化する場所のαのべき乗の差
			int colsdiff = 255 - h[r[j]][j];
			// α^0=α^255=1でない，かつ整数0(-1)でもないなら対角化を行う
			if ((h[r[j]][j] != 0) && (h[r[j]][j] != 255) && (h[r[j]][j] != -1)) {
				// 初回(j=0)は行成分全てに *α^diff 対角成分のみが0になる
				if (j == 0) {
					for (int i = 0; i < n; i++) {
						h[i][j] = (h[i][j] + colsdiff) % 255;
					}
				}
				// 初回以外，対角化した列は整数0*αのべき乗なので保持(この際，対角化して出来たα^0は通過しないので注意)
				else {
					for (int i = 0; i < n; i++) {
						flag = true;
						// これまでに対角化した列かどうか判定
						for (int num = 0; num < j; num++) {
							if (i == r[num]) {
								flag = false;
								break;
							}
						}
						// これまでに対角化していない列
						if (flag) {
							// j行目に整数0(-1)が無い
							if (h[i][j] != -1) {
								h[i][j] = (h[i][j] + colsdiff) % 255;
								flag = false;
							}
							// j行目に整数0(-1)が有る
							else {
								System.err.println("m行の該当箇所255化の際に-1が出現" + j
										+ "行 " + i + "列");
								flag = false;
							}
						}
					}
				}
			}
			// h[r[j]][]の列を対角成分以外全て整数0(-1)にする
			for (int s = 0; s < m; s++) {
				int rowsdiff = 0;
				// 対角化した行の成分の内，整数0(-1)にしたい成分
				int to0 = h[r[j]][s];
				// 対角成分h[r[j]][j]は0のまま保持 && 対角化した行の成分が整数0(-1)でない
				if (s != j && to0 != -1) {
					// 初回
					if (j == 0) {
						for (int i = 0; i < n; i++) {
							// 対角化した行成分にα^ivをかける → α^h[i][j] * α^iv
							// 対角成分はα^0なのでα^0 * α^iv = α^iv
							rowsdiff = (h[i][j] + to0) % 255;
							if (rowsdiff == 0) {
								// System.err.println("rowsdiff=0");
							}

							// 上記α^0 * α^iv = α^ivより整数0(-1)にしたい成分は rowsdiff ==
							// h[i][s]
							// α^rowsdiff+α^h[i][s] = 0でない → rowsdiff != h[i][s]
							if (rowsdiff != h[i][s]) {
								// ｈ[i][s] = α^rowsdiff + α^ｈ[i][s]
								// この値は整数0(-1)にならない
								h[i][s] = (g.toLog(g.toExp(rowsdiff)
										^ g.toExp(h[i][s]))) % 255;
							}
							// α^rowsdiff+α^h[i][s] = 0である → rowsdiff == h[i][s]
							else {
								// 整数0(-1)
								h[i][s] = -1;
							}
						}
					}

					// 初回以降
					else {
						for (int i = 0; i < n; i++) {
							flag = true;
							// 対角化が完了しているか確認
							for (int num = 0; num < j; num++) {
								if (i == r[num]) {
									flag = false;
									break;
								}
							}
							// 対角化が完了していない列
							if (flag) {
								// 一度，対角化され整数0(-1)となっている成分は除く
								if (h[i][j] != -1) {
									// 対角化した行成分にα^ivをかける → α^h[i][j] * α^iv
									// 対角成分はα^0なのでα^0 * α^iv = α^iv
									rowsdiff = (h[i][j] + to0) % 255;

									if (rowsdiff == -1) {
										System.err.println("rowsdiff == -1");
									}

									// h[i][s]が-1でなければ
									if (rowsdiff != -1 && h[i][s] != -1) {
										if (rowsdiff != h[i][s]) {
											// ｈ[i][s] = α^rowsdiff + α^ｈ[i][s]
											// この値は整数0(-1)にならない
											h[i][s] = (g.toLog(g
													.toExp(rowsdiff)
													^ g.toExp(h[i][s]))) % 255;
										}
										// α^rowsdiff+α^h[i][s] = 0である →
										// rowsdiff == h[i][s]
										else {
											// 整数0(-1)
											h[i][s] = -1;
										}
									}
									// h[i][s]が-1であれば整数0+α^rowsfiff
									else {
										h[i][s] = rowsdiff;
									}
								}
								// h[i][j] == -1
								else {
									System.err.println("一度，対角化されています");
								}
							}
						}
					}
				}
			}
		}

		// 検査行列と符号語の掛け算
		for (int i = 0; i < m; i++) {
			int answer = 0;
			for (int j = 0; j < n; j++) {
				flag = true;
				for (int d = 0; d < r.length; d++) {
					if (j == r[d]) {
						flag = false;
						break;
					}
				}
				if (flag) {
					if (h[j][i] != -1 && ((codewords[j] & 0xff) != 0)) {
						answer = answer ^ (g.toExp((h[j][i] + (g.toLog(codewords[j] & 0xff))) % 255));
					}
				}
			}
			cp[i] = (byte) answer;
		}

		// codewordsからcにデータを移す
		System.arraycopy(codewords, 0, c, 0, n);

		// cの転置cpの値をcに上書き
		for (int i = 0; i < m; i++) {
			c[r[i]] = cp[i];
		}
		return c;
	}

	// 規格によるリードソロモン符号の生成
	private static byte[] calculateRSECC3(byte[] codewords,
			byte rsEccCodewords, byte[] rsBlockOrder, int maxDataCodewords,
			int maxCodewords) {

		byte[][] rsCalTableArray = new byte[256][rsEccCodewords];
		try {
			String filename = QRCODE_DATA_PATH + "/rsc"
					+ Byte.toString(rsEccCodewords) + ".dat";
			InputStream fis = Qrcode.class.getResourceAsStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			for (int i = 0; i < 256; i++) {
				bis.read(rsCalTableArray[i]);
			}
			bis.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* ---- RS-ECC prepare */

		int i = 0;
		int j = 0;
		int rsBlockNumber = 0;

		byte[][] rsTemp = new byte[rsBlockOrder.length][];
		byte res[] = new byte[maxCodewords];
		System.arraycopy(codewords, 0, res, 0, codewords.length);
		i = 0;
		while (i < rsBlockOrder.length) {
			rsTemp[i] = new byte[(rsBlockOrder[i] & 0xFF) - rsEccCodewords];
			i++;
		}
		i = 0;
		while (i < maxDataCodewords) {
			rsTemp[rsBlockNumber][j] = codewords[i];
			j++;
			if (j >= (rsBlockOrder[rsBlockNumber] & 0xFF) - rsEccCodewords) {
				j = 0;
				rsBlockNumber++;
			}
			i++;
		}

		/* --- RS-ECC main --- */

		rsBlockNumber = 0;
		while (rsBlockNumber < rsBlockOrder.length) {
			byte[] rsTempData;
			rsTempData = (byte[]) rsTemp[rsBlockNumber].clone();

			int rsCodewords = (rsBlockOrder[rsBlockNumber] & 0xFF);
			int rsDataCodewords = rsCodewords - rsEccCodewords;

			j = rsDataCodewords;
			while (j > 0) {
				byte first = rsTempData[0];
				if (first != 0) {
					byte[] leftChr = new byte[rsTempData.length - 1];
					System.arraycopy(rsTempData, 1, leftChr, 0,
							rsTempData.length - 1);
					byte[] cal = rsCalTableArray[(first & 0xFF)];
					rsTempData = calculateByteArrayBits(leftChr, cal, "xor");
				} else {
					if (rsEccCodewords < rsTempData.length) {
						byte[] rsTempNew = new byte[rsTempData.length - 1];
						System.arraycopy(rsTempData, 1, rsTempNew, 0,
								rsTempData.length - 1);
						rsTempData = (byte[]) rsTempNew.clone();
					} else {
						byte[] rsTempNew = new byte[rsEccCodewords];
						System.arraycopy(rsTempData, 1, rsTempNew, 0,
								rsTempData.length - 1);
						rsTempNew[rsEccCodewords - 1] = 0;
						rsTempData = (byte[]) rsTempNew.clone();
					}
				}
				j--;
			}

			System.arraycopy(rsTempData, 0, res, /* codewords.length */
			maxDataCodewords + rsBlockNumber * rsEccCodewords, rsEccCodewords);
			rsBlockNumber++;
		}
		return res;
	}

	private static byte[] calculateByteArrayBits(byte[] xa, byte[] xb,
			String ind) {
		int ll;
		int ls;
		byte[] res;
		byte[] xl;
		byte[] xs;

		if (xa.length > xb.length) {
			xl = (byte[]) xa.clone();
			xs = (byte[]) xb.clone();
		} else {
			xl = (byte[]) xb.clone();
			xs = (byte[]) xa.clone();
		}
		ll = xl.length;
		ls = xs.length;
		res = new byte[ll];

		for (int i = 0; i < ll; i++) {
			if (i < ls) {
				if (ind == "xor") {
					res[i] = (byte) (xl[i] ^ xs[i]);
				} else {
					res[i] = (byte) (xl[i] | xs[i]);
				}
			} else {
				res[i] = xl[i];
			}
		}
		return res;
	}
}
/*--- class end ---*/