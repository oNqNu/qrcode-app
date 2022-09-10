package com.swetake.util;

import java.io.*;

public class calculate {
	BufferedReader myReader = new BufferedReader(new InputStreamReader(System.in), 1);
	DB db = new DB();

	int add(int x, int y) { // �����Z
		db.table();
		x = db.index(x);
		y = db.index(y);
		x = x ^ y; // 2 �̃x�N�g���\����XOR
		x = db.vectorDB[x]; // ���̌��ʂ��w���\���ɂ���
		System.out.println(x);
		return x;
	}

	int mul(int x, int y) { // �|���Z
		db.table();
		x = (x + y) % 255;
		x = db.vectorDB[x]; // �v�Z���ʂ��A�w���\���\����T��
		System.out.println(x);
		return x;
	}
}