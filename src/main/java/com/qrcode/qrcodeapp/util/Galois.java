package com.qrcode.qrcodeapp.util;

import java.util.Arrays;

/**
 * �^�C�g��: GF(2^8)
 *
 * @author Masayuki Miyazaki http://sourceforge.jp/projects/reedsolomon/
 */
public final class Galois {
	public static final int POLYNOMIAL = 0x1d;
	private static final Galois instance = new Galois();
	private int[] expTbl = new int[255 * 2]; // ��d�ɂ����Ƃɂ��mul, div�����ȗ���
	private int[] logTbl = new int[255 + 1];

	private Galois() {
		initGaloisTable();
	}

	public static Galois getInstance() {
		return instance;
	}

	/**
	 * �X�J���[�A�x�N�^�[�̑��ݕϊ��e�[�u���̍쐬
	 */
	private void initGaloisTable() {
		int d = 1;
		for (int i = 0; i < 255; i++) {
			expTbl[i] = expTbl[255 + i] = d;
			logTbl[d] = i;
			d <<= 1;
			if ((d & 0x100) != 0) {
				d = (d ^ POLYNOMIAL) & 0xff;
			}
		}
	}

	/**
	 * �X�J���[ -> �x�N�^�[�ϊ� ���n��(a)��b�恨01�n���(10�i�\��)
	 * 
	 * @param b
	 *            int
	 * @return int
	 */
	public int toExp(int b) {
		return expTbl[b];
	}

	/**
	 * �x�N�^�[ -> �X�J���[�ϊ� 01�n��(10�i�\��)b�����n��(a)�́H��
	 *
	 * @param b
	 *            int
	 * @return int
	 */
	public int toLog(int b) {
		return logTbl[b];
	}

	/**
	 * ���ʒu�C���f�b�N�X�̌v�Z
	 *
	 * @param length
	 *            int �f�[�^��
	 * @param a
	 *            int ���ʒu�x�N�^�[
	 * @return int ���ʒu�C���f�b�N�X
	 */
	public int toPos(int length, int a) {
		return length - 1 - logTbl[a];
	}

	/**
	 * �|���Z
	 *
	 * @param a
	 *            int
	 * @param b
	 *            int
	 * @return int = a * b
	 */
	public int mul(int a, int b) {
		return (a == 0 || b == 0) ? 0 : expTbl[logTbl[a] + logTbl[b]];
	}

	/**
	 * �|���Z
	 *
	 * @param a
	 *            int
	 * @param b
	 *            int
	 * @return int = a * ��^b
	 */
	public int mulExp(int a, int b) {
		return (a == 0) ? 0 : expTbl[logTbl[a] + b];
	}

	/**
	 * ����Z
	 *
	 * @param a
	 *            int
	 * @param b
	 *            int
	 * @return int = a / b
	 */
	public int div(int a, int b) {
		return (a == 0) ? 0 : expTbl[logTbl[a] - logTbl[b] + 255];
	}

	/**
	 * ����Z
	 *
	 * @param a
	 *            int
	 * @param b
	 *            int
	 * @return int = a / ��^b
	 */
	public int divExp(int a, int b) {
		return (a == 0) ? 0 : expTbl[logTbl[a] - b + 255];
	}

	/**
	 * �t��
	 *
	 * @param a
	 *            int
	 * @return int = 1/a
	 */
	public int inv(int a) {
		return expTbl[255 - logTbl[a]];
	}

	/**
	 * �����̊|���Z
	 *
	 * @param seki
	 *            int[] seki = a * b
	 * @param a
	 *            int[]
	 * @param b
	 *            int[]
	 */
	public void mulPoly(int[] seki, int[] a, int[] b) {
		Arrays.fill(seki, 0);
		for (int ia = 0; ia < a.length; ia++) {
			if (a[ia] != 0) {
				int loga = logTbl[a[ia]];
				int ib2 = Math.min(b.length, seki.length - ia);
				for (int ib = 0; ib < ib2; ib++) {
					if (b[ib] != 0) {
						seki[ia + ib] ^= expTbl[loga + logTbl[b[ib]]]; // =
																		// a[ia]
																		// *
																		// b[ib]
					}
				}
			}
		}
	}

	/**
	 * �V���h���[���̌v�Z
	 * 
	 * @param data
	 *            int[] ���̓f�[�^�z��
	 * @param length
	 *            int �f�[�^��
	 * @param syn
	 *            int[] (x - ��^0) (x - ��^1) (x - ��^2) ...�̃V���h���[��
	 * @return boolean true: �V���h���[���͑���0
	 */
	public boolean calcSyndrome(int[] data, int length, int[] syn) {
		int hasErr = 0;
		for (int i = 0; i < syn.length; i++) {
			int wk = 0;
			for (int idx = 0; idx < length; idx++) {
				wk = data[idx] ^ ((wk == 0) ? 0 : expTbl[logTbl[wk] + i]); // wk
																			// =
																			// data
																			// +
																			// wk
																			// *
																			// ��^i
			}
			syn[i] = wk;
			hasErr |= wk;
		}
		return hasErr == 0;
	}
}
