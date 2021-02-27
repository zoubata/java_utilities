package com.zoubworld.java.utils.compress.file;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Checksum;

import clojure.lang.IFn.L;

/**
 * 
 */

/**
 * @author Pierre Valleau class that compute redondancy information(K) and
 *         integrity information(N). result is : parityb[8] : parityB[8] :
 *         crc[16] : {0} : parityMCol[N] : parityMRow[N] parityb[8]=Xor(byte[i])
 *         parityB[i%8]=xor(SUM(i)Xor(byte[i])) CRC16
 *         parityMCol[i/N]=Xor(XorSum(i)(Long[i]))
 *         parityMRow[i%N]=Xor(XorSum(i)(Long[i]))
 * 
 */
public class Integrity {

	// fast xor table for byte.
	boolean xor8bit[];
	Crc16 crc;
	CRC64 crc64;

	/**
	 * 
	 */
	public Integrity() {
		init();
	}

	public Integrity(long size) {
		N = (int) Math.sqrt(Math.sqrt(size));
		if (N > 16)
			N = 16;
		if (N < 8)
			N = 8;
		N2 = N * N;

		K = (int) (size / N2 / 256);
		if (K == 0)
			K = 1;
		if (K > 512)
			K = 512;

		init();

	}

	public Integrity(int N, int K) {
		this.N = N;
		N2 = N * N;
		this.K = K;
		init();
	}

	private void init() {
		crc = new Crc16(Crc16.ccittRPoly, true);
		xor8bit = new boolean[256];
		for (int i = 0; i < 256; i++) {
			xor8bit[i] = false;
			for (int j = 0; j < 8; j++)
			{
				boolean b=(((i >> j) & 1) == 1);
				xor8bit[i] = (xor8bit[i] ^ b);
			}
				
		}

		{
			xorm = new long[N];
			xors = new long[N];
			for (int j = 0; j < 8; j++) {
				xorm[j] = 0L;
				xors[j] = 0L;
			}
			crc16 = 0;

		}
		cache = new long[K][][];
		for (int k = 0; k < K; k++) {
			cache[k] = new long[N][N];
			for (int x = 0; x < N; x++)
				for (int y = 0; y < N; y++)
					cache[k][x][y] = 0L;
		}
	}

	int kindex = 0;

	long cache[][][];
	int index = 0;
	int K = 64 * 4;
	int N = 16;
	int N2 = N * N;
	long xorm[];
	long xors[];
	int crc16 = 0x1234;

	public boolean xor(long l) {
		boolean b = true;
		for (int j = 0; j < 64; j += 8) {
			int i = (int) ((l >> j) & 0xff);
			b = b ^ xor8bit[i];
		}
		return b;
	}

	public long bool2long(boolean b[]) {
		long r = 0L;
		for (boolean e : b)
			r = (r << 1) | (e ? 1L : 0L);
		return r;
	}

	public boolean xor(char l) {
		return xor8bit[l];
	}

	public List<Long> getIntegrity() {
		return Integrity;
	}

	public long[][][] getRedondancy() {
		return cache;
	}

	List<Long> Integrity = new ArrayList<Long>();

	public Long process(long l[][]) {
		Long rl = null;
		for (long[] r : l)
			for (long c : r)
				rl = process(c);
		return rl;

	}

	/**
	 * provide an integrity steam and compute redondancy
	 */
	public Long process(long l) {
		cache[kindex][index / N][index % N] ^= l;
		xorm[index % N] = xorm[index % N] ^ l;
		xors[index / N] = xors[index / N] ^ l;
		crc16 = crc.calculate(l, crc16);
		index++;
		if (index == N2) {
			index = 0;
			boolean p8c[] = new boolean[N];
			for (int j = 0; j < N; j++)
				p8c[j] = xor(xorm[j]);

			boolean p8l[] = new boolean[N];
			for (int j = 0; j < N; j++)
				p8l[j] = xor(xors[j]);
			long w = 0;
			for (int j = 0; j < N; j++)
			{	w ^= (xors[j]);System.out.println("xors "+Long.toBinaryString(xors[j]));}
				System.out.println("w "+Long.toBinaryString(w));
			long b = 0;
			boolean p8b[] = new boolean[8];
			for (int j = 0; j < 64; j += 8) {
				long t = (long) ((w >> j) & 0xff);
				b = (long) (b ^ t);
				p8b[j / 8] = xor(t);
				System.out.println("t "+Long.toBinaryString(t)+" "+xor(t));
			}
			System.out.println("b "+Long.toBinaryString(bool2long(p8b)));
			long r = 0L;
			r = (r << 8) | bool2long(p8b);
			r = (r << 8) | b;
			r = (r << 16) | crc16;
			r = (r << N) | bool2long(p8l);
			r = (r << N) | bool2long(p8c);
			r = r << (32 - N - N);
			for (int j = 0; j < 8; j++) {
				xorm[j] = 0L;
				xors[j] = 0L;
			}
			crc16 = 0;
			kindex++;
			kindex = kindex % K;
			// p8b b crc16 / p8l p8c
			if (Integrity != null)
				Integrity.add(r);
			return r;
		}
		return null;
	}

	public boolean check(long integrity) {
		int M = ((1 << N) - 1);
		int parityb = (int) ((integrity >> (32 + 16 + 8)) & 0xff);
		int parityB = (int) ((integrity >> (32 + 16)) & 0xff);
		int parityMCol = (int) ((integrity >> N) & M);
		int parityMRow = (int) (integrity & M);
		int CRC16 = (int) ((integrity >> (32)) & 0xffff);
		System.out.println("parityb "+Long.toBinaryString(parityb)+":"+xor(parityb));
		System.out.println("parityB "+Long.toBinaryString(parityB)+":"+xor(parityB));
		System.out.println("parityMCol "+Long.toBinaryString(parityMCol)+":"+xor(parityMCol));
		System.out.println("parityMRow "+Long.toBinaryString(parityMRow)+":"+xor(parityMRow));
		
		boolean b = true;
		b = b && xor(parityb) == xor(parityB);
		b = b && xor(parityMCol) == xor(parityMRow);
		b = b && xor(parityb) == xor(parityMRow);
		return b;
	}

	/*
	 * return 0 when ok return -1 when integrity is corrupted
	 */
	public long check(long integrity, long[][] data) {
		if (!check(integrity))
			return -1;
		Integrity p = new Integrity(this.N, this.K);
		Long l = p.process(data);
		return l.longValue() ^ integrity;
	}

	public String map(long check) {
		String s = "";
		char r[][] = new char[N][N];
		char rb[][] = new char[8][8];
		for (int y = 0; y < N; y++)
			for (int x = 0; x < N; x++)
				r[y][x] = ' ';
		int M = ((1 << N) - 1);
		int parityb = (int) ((check >> (32 + 16 + 8)) & 0xff);

		int parityB = (int) ((check >> (32 + 16)) & 0xff);
		int parityMCol = (int) ((check >> N) & M);
		int parityMRow = (int) (check & M);
		int CRC16 = (int) ((check >> (32)) & 0xffff);

		for (int y = 0; y < N; y++)
			for (int x = 0; x < N; x++)
				r[y][x] = '.';
		for (int y = 0; y < N; y++)
			for (int x = 0; x < N; x++)
				if (((parityMRow) & (1 << y)) != 0)
					r[y][x] = '-';
		for (int y = 0; y < N; y++)
			for (int x = 0; x < N; x++)
				if (((parityMCol) & (1 << x)) != 0) {
					if (r[y][x] == '-')
						r[y][x] = '+';
					else
						r[y][x] = '|';
				}

		s += "word id :\r\n";
		// s+=r;
		for (int y = 0; y < N; y++) {
			for (int x = 0; x < N; x++)
				s += r[y][x];
			s += "\r\n";
		}

		for (int y = 0; y < 8; y++)
			for (int x = 0; x < 8; x++)
				rb[y][x] = '.';
		for (int y = 0; y < 8; y++)
			for (int x = 0; x < 8; x++)
				if (((parityB) & (1 << y)) != 0)
					rb[y][x] = '-';
		for (int y = 0; y < 8; y++)
			for (int x = 0; x < 8; x++)
				if (((parityb) & (1 << x)) != 0) {
					if (rb[y][x] == '-')
						rb[y][x] = '+';
					else
						rb[y][x] = '|';
				}
		s += "bit id :\r\n";
		// s+=r;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++)
				s += rb[y][x];
			s += "\r\n";
		}

		return s;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long t[] = new long[64 * 64 * 64 * 64];
		for (int i = 0; i < t.length; i++)
			t[i] = (long) (Math.random() * Long.MAX_VALUE);
		long prep = System.nanoTime();
		Integrity p = new Integrity(t.length);
		long start = System.nanoTime();
		Long l;
		for (int i = 0; i < t.length; i++) {
			l = p.process(t[i]);
			/*
			 * if (l!=null) System.out.println(l.toHexString(l));
			 */
		} // "END OF STREAM FD"
		System.out.println("time : " + (start - prep) / 1000 + "us");
		long stop = System.nanoTime();
		long d = (stop - start);
		System.out.println("t : " + t.length + " w ,  : " + t.length * 8 + " o,   : " + t.length / p.N2 + " NxN");

		System.out.println("K : " + p.K + ", N : " + p.N + ", NxN : " + p.N2 + " w,  size : " + p.N2 * 8 + " o");
		System.out.println("time : " + d / 1000000 + "ms");
		double speed = t.length;
		speed /= d;
		speed *= 1000L;
		System.out.println("speed : " + speed + " Mw/s");
		System.out.println("speed : " + speed * 8 + " Mo/s");
		System.out.println("cost integrity: " + (100.0 / p.N) / p.N + " %");
		System.out.println("cost redondancy: " + (100.0 * p.K * p.N2) / t.length + " %");
		long b[][] = new long[p.N][p.N];
		int i = 0;
		for (int y = 0; y < p.N; y++)
			for (int x = 0; x < p.N; x++)
				b[y][x] = t[i++];
		Long integrity = p.process(b);
		b[0][0] = b[0][0] ^ 0x010000L;
		long check = p.check(integrity, b);
		System.out.println(p.map(check));

		System.out.println("check:" + (check));
	}

	
}