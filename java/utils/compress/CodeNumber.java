package com.zoubworld.java.utils.compress;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.zoubworld.java.utils.compress.file.IBinaryReader;

/**
 * @author valleau Pierre
 *
 *         w: 2d+8h
 */
public class CodeNumber {

	public CodeNumber() {
		// TODO Auto-generated constructor stub
	}

	public static final int ExpGolombCoding = 1;

	// https://en.wikipedia.org/wiki/Exponential-Golomb_coding
	public static ICode getExpGolomb0Code(long n) {
		int k = 0;

		long N = (n + 1);
		int len = (int) (Math.log(N) / Math.log(2) + 1);
		if (n == 0)
			len = 0;
		len--;
		String s = "1";
		for (int i = len; i > 0; i--)
			s = "0" + s;
		for (int i = len - 1; i >= 0; i--)
			s += (N & (1 << i)) == (1 << i) ? "1" : "0";
		return new Code(s);
	}

	public static Long readExpGolomb0Code(IBinaryReader bin) {
		int l = 0;
		while (!bin.readBoolean())
			l++;
		long lo = 0;
		if (l > 0)
			lo = bin.readUnsignedLong(l);
		lo = (lo + (1 << l));
		lo--;
		return lo;
	}

	public static ICode getExpGolombkCode(int k, long n) {

		long N = ((n >> k) + 1);
		int len = (int) (Math.log(N) / Math.log(2) + 1);
		if (n == 0)
			len = 0;
		len--;
		String s = "1";
		for (int i = len; i > 0; i--)
			s = "0" + s;
		for (int i = len - 1; i >= 0; i--)
			s += (N & (1 << i)) == (1 << i) ? "1" : "0";

		for (int i = k - 1; i >= 0; i--)
			s += (n & (1 << i)) == (1 << i) ? "1" : "0";

		return new Code(s);
	}

	public static Long readExpGolombkCode(int k, IBinaryReader bin) {
		Long l = readExpGolomb0Code(bin);
		if (k > 0) {
			l = l << k;
			l += bin.readUnsignedLong(k);
		}
		return l;
	}

	public static final int ExpGolomb1OrderCoding = 2;
	public static final int ExpGolomb2OrderCoding = 3;
	public static final int ExpGolomb3OrderCoding = 4;
	public static final int ExpGolomb4OrderCoding = 5;

	
	// https://en.wikipedia.org/wiki/Unary_coding
	public static ICode getUnaryCode(long n) {
		String s = "0";
		/*for (int i = 0; i < n; i++)
			s = "1" + s;*/
		s=StringUtils.repeat("1",(int) n)+"0";
		return new Code(s);
	}

	public static Long readUnaryCode(IBinaryReader bin) {
		Long l = 0L;
		while (bin.readBoolean())
			l++;
		return l;
	}

	public static final int PhaseInCoding = 6;

	// https://fr.wikipedia.org/wiki/Codage_binaire_tronqu%C3%A9
	// https://en.wikipedia.org/wiki/Truncated_binary_encoding
	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getPhaseInCode(int n, long x) {
		assert (x < n);
		int k = (int) (Math.log(n) / Math.log(2));
		int u = (int) (Math.pow(2, k + 1) - n);
		if (x < u)
			return new Code(x, k);
		else
			return new Code(x + u, k + 1);
	}

	public static Long readPhaseInCode(int n, IBinaryReader bin) {
		int k = (int) Math.floor(Math.log(n) / Math.log(2));
		int u = (int) (Math.pow(2, k + 1) - n);
		Long l = 0L;
		if (k>0)
			l=bin.readUnsignedLong(k);
		if (l >= u) {
			l = (l << 1) + (bin.readBoolean() ? 1 : 0);
			l -= u;
		}

		return l;
	}

	public static final int GammaCoding = 7;

	// NOT THAT https://fr.wikipedia.org /wiki /Codage_gamma
	// https://en.wikipedia.org/wiki/Elias_gamma_coding
	/**
	 * x: symbol inside the alphabet
	 */
	public static ICode getGammaCode(long x) {
		/*
		 * public static ICode getGammaCode(long n) {
		 * 
		 * int u; if(n==0) return null; else u=(int) (Math.log(n)/Math.log(2)); long
		 * B=n-1<<(u);
		 * 
		 * 
		 * return new Code(getUnaryCode(u),new Code(B,u));
		 */
		int n;
		if (x == 0)
			return null;
		else
			n = (int) (Math.log(x) / Math.log(2));
		return new Code(new Code(0, n), new Code(x, n + 1));
	}

	public static Long readGammaCode(IBinaryReader bin) {
		int c = 0;
		while (!bin.readBoolean())
			c++;

		long B = 1 << c;
		if (c > 0)
			B += bin.readUnsignedLong(c);
		return B;
		/*
		 * Long u=readUnaryCode(bin); int B=bin.readInt(u.intValue()); long n=B+1<<u;
		 * return n;
		 */
	}

	public static final int DeltaCoding = 8;
	// https://fr.wikipedia.org/wiki/Codage_delta

	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getDeltaCode(long n) {

		int g;
		if (n > 0)
			g = (int) Math.floor(Math.log(n) / Math.log(2));
		else
			return null;
		long B = n - (1 << (g));
		return new Code(getGammaCode(g + 1), new Code(B, g));
	}

	public static Long readDeltaCode(IBinaryReader bin) {
		/*
		 * long g=readGammaCode(bin)-1; long B=bin.readLong((int)g); long n=B+(1<<g);
		 * return n;
		 */
		/*
		 * int L=0; while(!bin.readBoolean()) L++;
		 * 
		 * long N1=1<<L; if (L>0) N1+=bin.readLong(L); return N1-1;
		 */

		Long g = readGammaCode(bin) - 1;
		long B = 0;
		if (g.intValue() > 0)
			B = bin.readUnsignedLong(g.intValue());
		long n = B + (1 << g);
		return n;
	}

	public static final int OmegaCoding = 9;
	// https://en.wikipedia.org/wiki/Elias_omega_coding

	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getOmegaCode(long n) {
		if (n <= 0)
			return null;
		// 1
		Code c = new Code("0");
		// 2
		if (n == 1)
			return c;
		int len = (int) (Math.log(n) / Math.log(2) + 1);
		while (len >= 2)// 5 Return to step 2 to prepend the encoding of the new N.
		{
			// 2
			if (n == 1)
				return c;
			// 3
			assert (len >= 2);// This will be at least two bits,
			Code c2 = new Code(n, len);// the first bit of which is a 1
			c = new Code(c2, c);// Prepend the binary representation of N to the beginning of the code.
			n = len - 1;// Let N equal the number of bits just prepended, minus one.
			len = (int) (Math.log(n) / Math.log(2) + 1);
		}
		return c;
	}

	public static Long readOmegaCode(IBinaryReader bin) {
		long N = 1; // Start with a variable N, set to a value of 1.
		Boolean b = bin.readBoolean();

		while (true)

			if (!b)// If the next bit is a "0", stop. The decoded number is N.
			{
				// N=(1<<N)+((N==1)?0L:bin.readLong((int)(N-1)));
				return N;
			} else
			// If the next bit is a "1", then read it plus N more bits,
			// and use that binary number as the new value of N.
			// Go back to step 2.
			{
				N = (1 << N) + bin.readUnsignedLong((int) (N));
				b = bin.readBoolean();
			}

	}

	//static final int RiceCoding = 10;
	public static final int Rice0Coding = 28;
	public static final int Rice1Coding = 29;
	public static final int Rice2Coding = 30;
	public static final int Rice3Coding = 31;
	public static final int Rice4Coding = 32;
	public static final int Rice5Coding = 27;
	public static final int Rice6Coding = 26;
	public static final int Rice7Coding = 25;
	public static final int Rice8Coding = 24;
	// https://fr.wikipedia.org/wiki/Codage_de_Rice

	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getRiceCode(int k, long n) {

		return getGolombkCode(1 << k, n);
	}

	public static Long readRiceCode(int k, IBinaryReader bin) {
		return readGolombkCode(1 << k, bin);
	}

	public static final int Golomb1Coding = 19;
	public static final int Golomb2Coding = 20;
	public static final int Golomb4Coding = 21;
	public static final int Golomb8Coding = 22;
	public static final int Golomb16Coding = 23;
	// https://fr.wikipedia.org/wiki/Codage_de_Golomb

	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getGolombkCode(int k, long n) {
		if (k <= 0)
			return null;
		long q = n / k;
		long r = n - q * k;
		int l = 0;
		k = k - 1;
		while (k != 0) {
			l++;
			k = k >> 1;
		}
		// l++;

		return new Code(getUnaryCode(q), new Code(r, l));
	}

	public static Long readGolombkCode(int k, IBinaryReader bin) {
		int l = 0;
		if (k <= 0)
			return null;
		int K = k;
		k = k - 1;
		while (k != 0) {
			l++;
			k = k >> 1;
		}
		// l++;
		Long q = readUnaryCode(bin);
		long r = 0;
		if (l > 0)
			r = bin.readUnsignedLong(l);
		long n = q * K + r;
		return n;
	}

	public static final int FibonacciCoding = 12;
	// https://fr.wikipedia.org/wiki/Codage_de_Fibonacci
	static List<Long> fib = new ArrayList();

	public static long Fib(int n) {
		for (int i = fib.size(); i < n + 1; i++)
			if (i == 0)
				fib.add(0L);
			else if (i == 1)
				fib.add(1L);
			else
				fib.add(fib.get(i - 2) + fib.get(i - 1));
		return fib.get(n);
	}

	/**
	 * n: symbol inside the alphabet
	 */
	public static ICode getFibonacciCode(long N) {
		long n = N;
		if (n == 0)
			return null;

		int i = 0;
		long r = 0;
		String s = " ";
		int l = 0;
		// find i, bigest fib(i) close to n
		for (i = 0; Fib(i + 2) <= n; i++)
			;
		i--;
		// split n in fib(i)
		for (int j = i; j >= 0; j--) {
			if (n >= Fib(j + 2)) {
				n -= Fib(j + 2);
				r = r + (1 << l);
				s = "1" + s;
			} else
				s = "0" + s;
			l++;
		}
		s = s + "1";
		l++;
		r = (r << 1) + 1;

		return new Code(r, l);
	}

	public static Long readFibonacciCode(IBinaryReader bin) {

		boolean b = false;
		boolean bold = false;
		int l = 0;
		long r = 0;
		while (!((b = bin.readBoolean() == true) && (bold == true))) {
			if (b)
				r += Fib(l + 2);
			l++;
			bold = b;
		}

		return r;
	}

	public static long fibonacci(int n) {
		if (n == 0)
			return 0;
		if (n <= 1)
			return 1;
		if (n == 2)
			return 1;
		return fibonacci(n - 1) + fibonacci(n - 2);
	}

	public static final int Zeta1Coding = 16;
	public static final int Zeta2Coding = 13;
	public static final int Zeta3Coding = 11;
	public static final int Zeta4Coding = 15;
	public static final int UnaryCoding = 33;
	
	public static final int GolombkCoding = 37;
	public static final int ZetaCoding = 38;
	public static final int MaxCodingIndex = 33;

	// https://fr.wikipedia.org/wiki/Codage_zeta
	
	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getZetaCode(int k, long n) {

		if (n <= 0)
			return null;
	/*	int h = (int) (Math.log(n) / Math.log(2));
		
		long k2 = 1 << k;
		k2 = (int) Math.pow(Math.pow(2, k), h);
		
		// N=(N<0)?n-1:N;//pos or null
		int l = k + h - 1;
		
		l = h + k-1;
		h = h/k ;
		//if (h!=0)
		k2 = (int) Math.pow(Math.pow(2, k), h);
		//else k2=0;
		long N = n - k2;
		return new Code(getUnaryCode(h), new Code(N, l));*/
		
		int l2N = (int) (Math.log(n) / Math.log(2));
		int h=l2N/k;
		int r=l2N-h;
		int k2=(int) Math.pow(Math.pow(2, k), h);
		int k3=(int) Math.pow(Math.pow(2, k), h+1);
		
		long Nt=n-k2-0;
		int L= (k+1)*(h+1)-(r<1?1:0);
		return new Code(getUnaryCode(h), getPhaseInCode(k3-k2, Nt));
	}

	public static Long readZetaCode(int k, IBinaryReader bin) {

		int h = readUnaryCode(bin).intValue();
		int k2=(int) Math.pow(Math.pow(2, k), h);
		int k3=(int) Math.pow(Math.pow(2, k), h+1);
		int n=k3-k2;
		Long l = 0L;
		if (n>0)
		{			
			int kk = (int) Math.floor(Math.log(n) / Math.log(2));
			int u = (int) (Math.pow(2, kk + 1) - n);			
			if (kk>0)
				l=bin.readUnsignedLong(kk);
			if (l >= u) {
				l = (l << 1) + (bin.readBoolean() ? 1 : 0);
				l -= u;
			}
		}
		

		return l + k2;
	}

	public static final int EvenRodehCoding = 14;
	// https://fr.wikipedia.org/wiki/Codage_d%27Even-Rodeh

	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getEvenRodehCode(long n) {
		Code c;
		// 1 If N is not less than 4 then set the coded value to a single 0 bit.
		// Otherwise the coded value is empty.
		if (n >= 4)
			c = new Code("0");
		else
			c = new Code(0, 0);
		while (true) {
			// 2 If N is less than 8 then prepend the coded value with 3 bits containing the
			// value of N and stop.
			if (n < 8) {
				c = new Code(new Code(n, 3), c);
				return c;
			}
			// 3 Prepend the coded value with the binary representation of N.
			int M = (int) (Math.log(n) / Math.log(2)) + 1;
			c = new Code(new Code(n, M), c);
			// 4 Store the number of bits prepended in step 3 as the new value of N.
			n = M;
			// 5 Go back to step 2.
		}
	}

	public static Long readEvenRodehCode(IBinaryReader bin) {
		// 1 Read 3 bits and store the value into N
		long n = bin.readUnsignedInt(3);
		if ((n & 0x4) == 0)
			return n;
		// If the first bit read was 0 then stop. The decoded number is N.
		// If the first bit read was 1 then continue to step 2.
		// 2 Examine the next bit.
		while (true) {
			// If the bit is 0 then read 1 bit and stop. The decoded number is N.
			// If the bit is 1 then read N bits, store the value as the new value of N, and
			// go back to step 2.
			if (!bin.readBoolean())
				return n;
			else
				n = (1 << (n - 1)) + bin.readUnsignedLong((int) n - 1);
		}

	}

	public static final int LevenshteinCoding = 17;
	// https://fr.wikipedia.org/wiki/Codage_de_Levenshtein

	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getLevenshteinCode(long n) {
		// The code of zero is "0"; to code a positive number:
		if (n == 0)
			return new Code("0");
		Code co = new Code(0, 0);
		int M = 1;
		// Initialize the step count variable C to 1.
		int c = 1;
		while (M > 0) {
			// 2 Write the binary representation of the number without the leading "1" to
			// the beginning of the code.
			M = 0;
			if (n > 1)
				M = (int) (Math.log(n) / Math.log(2));

			co = new Code(new Code(n, M), co);
			// Let M be the number of bits written in step 2.
			// If M is not 0, increment C, repeat from step 2 with M as the new number.
			if (M != 0) {
				c++;
				n = M;
			}
		}

		// Write C "1" bits and a "0" to the beginning of the code.
		co = new Code(new Code("0"), co);
		co = new Code(new Code(-1, c), co);
		return co;
	}

	public static Long readLevenshteinCode(IBinaryReader bin) {

		int c = 0;
		long N = 1;// 3 Start with a variable N, set it to a value of 1
		// 1 Count the number of "1" bits until a "0" is encountered.

		while (bin.readBoolean())
			c++;

		// 2 If the count is zero, the value is zero, otherwise
		if (c == 0)
			return N = 0L;

		// 3 and repeat count minus 1 times:
		N = 1L;
		for (int i = 0; i < c - 1; i++) {

			int val = 1 << N;
			if (c > 0)
				val |= bin.readUnsignedLong((int) N);
			N = val;
		}
		// 4 Read N bits, prepend "1", assign the resulting value to N
		return N;
	}

	public static final int VLQCoding = 18;
	// https://en.wikipedia.org/wiki/Variable-length_quantity

	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getVLQCode(long n) {
		if (n == 0)
			return new Code(0, 8);
		Code c = new Code(0, 0);
		int f = 0;
		while (n > 0) {
			c = new Code(new Code(new Code(f, 1), new Code(n & 0x7F, 7)), c);
			n = n >> 7;
			f = 1;
		}
		return c;
	}

	public static Long readVLQCode(IBinaryReader bin) {
		int b = bin.readUnsignedInt(8);
		long r = 0L;
		while ((b & 0x80) != 0) {
			r = (r << 7) + (b & 0x7f);
			b = bin.readUnsignedInt(8);
		}
		r = (r << 7) + (b & 0x7f);
		return r;
	}

	public static final int LZ4Coding = 10;

	// https://github.com/lz4/lz4/blob/master/doc/lz4_Block_format.md
	// Byte coding : https://en.wikipedia.org/wiki/Universal_code_(data_compression)
	// 0..F F00 FFF F FF FF
	// 0..15 ; 16..15+255 281.. 15+255+255
	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getLZ4Code(long n) {
		ICode c = new Code(0, 0);

		if (n >= 15) {
			c = new Code(c, new Code(15, 4));
			n -= 15;
		} else
			return new Code(n, 4);
		while (n >= 255) {
			c = new Code(c, new Code(Math.min(n, 255), 8));
			n -= 255;
		}
		c = new Code(c, new Code(n, 8));
		return c;
	}

	public static Long readLZ4Code(IBinaryReader bin) {
		long n = 0;
		long b = bin.readUnsignedLong(4);
		n += b;
		if (b >= 15) {
			while ((b = bin.readUnsignedLong(8)) >= 255)
				n += b;

			n += b;
		}
		return n;
	}

	public static final int NibblesCoding = 0;
	

	// https://github.com/lz4/lz4/blob/master/doc/lz4_Block_format.md
	// Byte coding : https://en.wikipedia.org/wiki/Universal_code_(data_compression)
	// nibbles codding :
	/*
	 * 0..3 ; C..F=7 ; 3C..3F=11 : FC..FF=15 0..F ; F0..FF=31 ; FF0..FFF=47 00..FF ;
	 * FF00..FFFF=511
	 * 
	 * LEN:bitStream
	 */

	/**
	 * n : size of alphabet x: symbol inside the alphabet
	 */
	public static ICode getNibblesCode(long n) {

		ICode c = new Code(0, 0);

		if (n >= 15) {
			c = new Code(c, new Code(15, 4));
			n -= 15;
		} else
			return new Code(n, 4);
		while (n >= 15) {
			c = new Code(c, new Code(Math.min(n, 15), 4));
			n -= 15;
		}
		c = new Code(c, new Code(n, 4));
		return c;

	}

	public static Long readNibblesCode(IBinaryReader bin) {

		long n = 0;
		long b = 0;
		while ((b = bin.readUnsignedLong(4)) >= 15)
			n += b;

		n += b;

		return n;

	}

	// custom SL3+X
	/*
	 * 0bSLLLxxxx xxxx xxxx S 0 x>=0 1 x<0 LLL : 0 only xxxx 0..15 1 xxxx xxxx
	 * 16...16+255 2 xxxx xxxx xxxx 16+256+0..4096 ... 7 xxxx X8 => 2**64
	 */
	// custom L2+X...
	/*
	 * LL xx..... LL : 0 xx 0..3 1 xxxxxx 4..63+4 2 XX XXXX XXXXXXXX : 4+64+0..16383
	 * 3 XX XXXX XXXXXXXX xxxxXXXXxxxxXXXX : 4+64+16384+0..2**30
	 * 
	 * RPT len, numberTime
	 * 
	 */

	public static void main(String[] args) {
		int i = 0;
		System.out.print("i");
		for (int j = 0; j <= 40; j++)
			System.out.print(", getCode(" + j + " i)");
		System.out.println();
		ICode c;
		for (i = 0; i < 1000; i++) {
			System.out.print(i);
			for (int j = 0; j <= 40; j++)

				System.out.print(
						", " + ((((c = getCode(j, i)) != null) && (c.toRaw() != null)) ? c.toRaw().length() : ""));
			System.out.println();
		}
	}

	public static Long readCode(int keycode, IBinaryReader num) {
		switch (keycode) {
		case CodeNumber.ExpGolombCoding:
			return readExpGolomb0Code(num);
		case CodeNumber.DeltaCoding:
			return readDeltaCode(num);
		case CodeNumber.EvenRodehCoding:
			return readEvenRodehCode(num);
		case CodeNumber.ExpGolomb1OrderCoding:
			return readExpGolombkCode(1, num);
		case CodeNumber.ExpGolomb2OrderCoding:
			return readExpGolombkCode(2, num);
		case CodeNumber.ExpGolomb3OrderCoding:
			return readExpGolombkCode(3, num);
		case CodeNumber.ExpGolomb4OrderCoding:
			return readExpGolombkCode(4, num);
		case CodeNumber.FibonacciCoding:
			return readFibonacciCode(num);
		case CodeNumber.GammaCoding:
			return readGammaCode(num);
		case CodeNumber.Golomb1Coding:
			return readGolombkCode(1, num);
		case CodeNumber.Golomb2Coding:
			return readGolombkCode(2, num);
		case CodeNumber.Golomb4Coding:
			return readGolombkCode(4, num);
		case CodeNumber.Golomb8Coding:
			return readGolombkCode(8, num);
		case CodeNumber.Golomb16Coding:
			return readGolombkCode(16, num);
		case CodeNumber.LevenshteinCoding:
			return readLevenshteinCode(num);
		case CodeNumber.LZ4Coding:
			return readLZ4Code(num);
		case CodeNumber.NibblesCoding:
			return readNibblesCode(num);
		case CodeNumber.OmegaCoding:
			return readOmegaCode(num);
		case CodeNumber.PhaseInCoding:
			return readPhaseInCode(Symbol.getNbSymbol(), num);
		case CodeNumber.Rice0Coding:
			return readRiceCode(0, num);
		case CodeNumber.Rice1Coding:
			return readRiceCode(1, num);
		case CodeNumber.Rice2Coding:
			return readRiceCode(2, num);
		case CodeNumber.Rice3Coding:
			return readRiceCode(3, num);
		case CodeNumber.Rice4Coding:
			return readRiceCode(4, num);
		case CodeNumber.Rice5Coding:
			return readRiceCode(5, num);
		case CodeNumber.Rice6Coding:
			return readRiceCode(6, num);
		case CodeNumber.Rice7Coding:
			return readRiceCode(7, num);
		case CodeNumber.Rice8Coding:
			return readRiceCode(8, num);
		case CodeNumber.UnaryCoding:
			return readUnaryCode(num);
		case CodeNumber.VLQCoding:
			return readVLQCode(num);
		case CodeNumber.Zeta1Coding:
			return readZetaCode(1, num);
		case CodeNumber.Zeta2Coding:
			return readZetaCode(2, num);
		case CodeNumber.Zeta3Coding:
			return readZetaCode(3, num);			
		case CodeNumber.Zeta4Coding:
			return readZetaCode(4, num);

		default:
			break;
		}
		return null;
	}

	public static ICode getCode(int keycode, long num) {
		switch (keycode) {
		case CodeNumber.ExpGolombCoding:
			return getExpGolomb0Code(num);
		case CodeNumber.DeltaCoding:
			return getDeltaCode(num);
		case CodeNumber.EvenRodehCoding:
			return getEvenRodehCode(num);
		case CodeNumber.ExpGolomb1OrderCoding:
			return getExpGolombkCode(1, num);
		case CodeNumber.ExpGolomb2OrderCoding:
			return getExpGolombkCode(2, num);
		case CodeNumber.ExpGolomb3OrderCoding:
			return getExpGolombkCode(3, num);
		case CodeNumber.ExpGolomb4OrderCoding:
			return getExpGolombkCode(4, num);
		case CodeNumber.FibonacciCoding:
			return getFibonacciCode(num);
		case CodeNumber.GammaCoding:
			return getGammaCode(num);
		case CodeNumber.Golomb1Coding:
			return getGolombkCode(1, num);
		case CodeNumber.Golomb2Coding:
			return getGolombkCode(2, num);
		case CodeNumber.Golomb4Coding:
			return getGolombkCode(4, num);
		case CodeNumber.Golomb8Coding:
			return getGolombkCode(8, num);
		case CodeNumber.Golomb16Coding:
			return getGolombkCode(16, num);
		case CodeNumber.LevenshteinCoding:
			return getLevenshteinCode(num);
		case CodeNumber.LZ4Coding:
			return getLZ4Code(num);
		case CodeNumber.NibblesCoding:
			return getNibblesCode(num);
		case CodeNumber.OmegaCoding:
			return getOmegaCode(num);
		case CodeNumber.PhaseInCoding:
			return getPhaseInCode(Symbol.getNbSymbol(), num);
		case CodeNumber.Rice0Coding:
			return getRiceCode(0, num);
		case CodeNumber.Rice1Coding:
			return getRiceCode(1, num);
		case CodeNumber.Rice2Coding:
			return getRiceCode(2, num);
		case CodeNumber.Rice3Coding:
			return getRiceCode(3, num);
		case CodeNumber.Rice4Coding:
			return getRiceCode(4, num);
		case CodeNumber.Rice5Coding:
			return getRiceCode(5, num);
		case CodeNumber.Rice6Coding:
			return getRiceCode(6, num);
		case CodeNumber.Rice7Coding:
			return getRiceCode(7, num);
		case CodeNumber.Rice8Coding:
			return getRiceCode(8, num);
		case CodeNumber.UnaryCoding:
			return getUnaryCode(num);
		case CodeNumber.VLQCoding:
			return getVLQCode(num);
		case CodeNumber.Zeta1Coding:
			return getZetaCode(1, num);
		case CodeNumber.Zeta2Coding:
			return getZetaCode(2, num);
		case CodeNumber.Zeta3Coding:
			return getZetaCode(3, num);
		case CodeNumber.Zeta4Coding:
			return getZetaCode(4, num);

		default:
			break;
		}
		return null;
	}
	public static String  getName(int keycode) {
		switch (keycode) {
		case CodeNumber.ExpGolombCoding:
			return "ExpGolombCoding";
		case CodeNumber.DeltaCoding:
			return "DeltaCoding";
		case CodeNumber.EvenRodehCoding:
			return "EvenRodehCoding";
		case CodeNumber.ExpGolomb1OrderCoding:
			return "ExpGolomb1OrderCoding";
		case CodeNumber.ExpGolomb2OrderCoding:
			return "ExpGolomb2OrderCoding";
		case CodeNumber.ExpGolomb3OrderCoding:
			return "ExpGolomb3OrderCoding";
		case CodeNumber.ExpGolomb4OrderCoding:
			return "ExpGolomb4OrderCoding";
		case CodeNumber.FibonacciCoding:
			return "FibonacciCoding";
		case CodeNumber.GammaCoding:
			return "GammaCoding";
		case CodeNumber.Golomb1Coding:
			return "Golomb1Coding";
		case CodeNumber.Golomb2Coding:
			return "Golomb2Coding";
		case CodeNumber.Golomb4Coding:
			return "Golomb4Coding";
		case CodeNumber.Golomb8Coding:
			return "Golomb8Coding";
		case CodeNumber.Golomb16Coding:
			return "Golomb16Coding";
		case CodeNumber.LevenshteinCoding:
			return "LevenshteinCoding";
		case CodeNumber.LZ4Coding:
			return "LZ4Coding";
		case CodeNumber.NibblesCoding:
			return "NibblesCoding";
		case CodeNumber.OmegaCoding:
			return "OmegaCoding";
		case CodeNumber.PhaseInCoding:
			return "PhaseInCoding";
		case CodeNumber.Rice0Coding:
			return "Rice0Coding";
		case CodeNumber.Rice1Coding:
			return "Rice1Coding";
		case CodeNumber.Rice2Coding:
			return "Rice2Coding";
		case CodeNumber.Rice3Coding:
			return "Rice3Coding";
		case CodeNumber.Rice4Coding:
			return "Rice4Coding";
		case CodeNumber.Rice5Coding:
			return "Rice5Coding";
		case CodeNumber.Rice6Coding:
			return "Rice6Coding";
		case CodeNumber.Rice7Coding:
			return "Rice7Coding";
		case CodeNumber.Rice8Coding:
			return "Rice8Coding";
		case CodeNumber.UnaryCoding:
			return "UnaryCoding";
		case CodeNumber.VLQCoding:
			return "VLQCoding";
		case CodeNumber.Zeta1Coding:
			return "Zeta1Coding";
		case CodeNumber.Zeta2Coding:
			return "Zeta2Coding";
		case CodeNumber.Zeta3Coding:
			return "Zeta3Coding";
		case CodeNumber.Zeta4Coding:
			return "Zeta4Coding";

		default:
			break;
		}
		return "#?";
	}
}
