// package com.qrcode.qrcodeapp.classes;

// /*
// Sfmt.java �������C�u�����ȈՔ�
// coded by isaku@pb4.so-net.ne.jp
// */

// class Sfmt {
// 	int index;
// 	int[] x = new int[624]; /* ��ԃe�[�u�� */

// 	void gen_rand_all() {
// 		int a = 0, b = 488, c = 616, d = 620, y;
// 		int[] p = x;

// 		do {
// 			y = p[a + 3] ^ (p[a + 3] << 8) ^ (p[a + 2] >>> 24) ^ ((p[b + 3] >>> 11) & 0xbffffff6);
// 			p[a + 3] = y ^ (p[c + 3] >>> 8) ^ (p[d + 3] << 18);
// 			y = p[a + 2] ^ (p[a + 2] << 8) ^ (p[a + 1] >>> 24) ^ ((p[b + 2] >>> 11) & 0xbffaffff);
// 			p[a + 2] = y ^ ((p[c + 2] >>> 8) | (p[c + 3] << 24)) ^ (p[d + 2] << 18);
// 			y = p[a + 1] ^ (p[a + 1] << 8) ^ (p[a] >>> 24) ^ ((p[b + 1] >>> 11) & 0xddfecb7f);
// 			p[a + 1] = y ^ ((p[c + 1] >>> 8) | (p[c + 2] << 24)) ^ (p[d + 1] << 18);
// 			y = p[a] ^ (p[a] << 8) ^ ((p[b] >>> 11) & 0xdfffffef);
// 			p[a] = y ^ ((p[c] >>> 8) | (p[c + 1] << 24)) ^ (p[d] << 18);
// 			c = d;
// 			d = a;
// 			a += 4;
// 			b += 4;
// 			if (b == 624)
// 				b = 0;
// 		} while (a != 624);
// 	}

// 	void period_certification() {
// 		int work, inner = 0, i, j;
// 		int[] parity = { 0x00000001, 0x00000000, 0x00000000, 0x13c9e684 };

// 		index = 624;
// 		for (i = 0; i < 4; i++)
// 			inner ^= x[i] & parity[i];
// 		for (i = 16; i > 0; i >>>= 1)
// 			inner ^= inner >>> i;
// 		inner &= 1;
// 		if (inner == 1)
// 			return;
// 		for (i = 0; i < 4; i++)
// 			for (j = 0, work = 1; j < 32; j++, work <<= 1)
// 				if ((work & parity[i]) != 0) {
// 					x[i] ^= work;
// 					return;
// 				}
// 	}

// 	/* �����̎� s �ɂ�鏉�� */
// 	public synchronized void InitMt(int s) {
// 		x[0] = s;
// 		for (int p = 1; p < 624; p++)
// 			x[p] = s = 1812433253 * (s ^ (s >>> 30)) + p;
// 		period_certification();
// 	}

// 	Sfmt(int s) {
// 		InitMt(s);
// 	}

// 	/* 32�r�b�g�������萮���̗��� */
// 	public synchronized int NextMt() {
// 		if (index == 624) {
// 			gen_rand_all();
// 			index = 0;
// 		}
// 		return x[index++];
// 	}

// 	/* �O�ȏ� n �����̐������� */
// 	public int NextInt(int n) {
// 		double z = NextMt();
// 		if (z < 0)
// 			z += 4294967296.0;
// 		return (int) (n * (1.0 / 4294967296.0) * z);
// 	}

// 	/* �O�ȏ�P�����̗���(53bit���x) */
// 	public double NextUnif() {
// 		double z = NextMt() >>> 11, y = NextMt();
// 		if (y < 0)
// 			y += 4294967296.0;
// 		return (y * 2097152.0 + z) * (1.0 / 9007199254740992.0);
// 	}

// }
