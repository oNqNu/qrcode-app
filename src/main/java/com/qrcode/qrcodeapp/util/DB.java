package com.qrcode.qrcodeapp.util;

public class DB {
	int indexDB[] = new int[256]; // �w���\�����x�N�g���\���̕\
	int vectorDB[] = new int[256]; // �x�N�g���\�����w���\���̕\

	void table() {
		int a, v = 00000001;
		for (a = 0; a <= 254; a++) { // �w���\������x�N�g���\���������\
			if (v > 255) { // 9 ���ڂ����������� (v > 11111111)
				v = (v & 255) ^ 29;
				// 9 ���ڂ������ă�^4+��^3+��^2+1(��^8)��29(11101) ��xor ���Ƃ�
			}
			indexDB[a] = v;
			v = v << 1;
		}
		indexDB[255] = 00000000;
		vectorDB[0] = 255;
		for (a = 1; a <= 254; a++) { // �x�N�g���\������w���\���������\
			v = indexDB[a];
			vectorDB[v] = a;
		}
		for (a = 0; a <= 255; a++) {
		}
	}

	int index(int x) {
		x = indexDB[x];
		return x;
	}

	int vector(int x) {
		x = vectorDB[x];
		return x;
	}
}
