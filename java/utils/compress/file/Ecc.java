package com.zoubworld.java.utils.compress.file;

import java.util.Arrays;

public class Ecc {

	short FAST_parity[] = null;

	public byte getParity(byte[] word) {
		if (FAST_parity == null) {
			getECC(word);
		}
		short byteb = 0;
		int lw = word.length;
		for (int i = 0; i < lw; i++)
			byteb ^= word[i];
		System.out.println("byteb=" + byteb);
		byteb = (byte) FAST_parity[(short) byteb & 0xFF];// 0

		System.out.println("byteb=" + byteb);
		return (byte) byteb;
	}

	/**
	 * return the corected word based on eccdata word max size is 64 byte
	 */
	public byte[] getdata(byte[] word, byte[] ecc) {
		byte[] ecc2 = getECC(word);
		if (Arrays.equals(ecc2, ecc))
			return word;

		if (ecc.length < 8) {
			/*
			 * if((ecc[0]&0x1)==(ecc2[0]&0x1)) return null;//more than 1 error
			 */ long lecc = 0;
			long lecc2 = 0;

			for (int i = 0; i < ecc.length; i++) {
				lecc |= ecc[i] << (i * 8);
				lecc2 |= ecc2[i] << (i * 8);
				/*
				 * 23=17=00010111 112=70=01110000 -XX--XXX
				 * 
				 * word w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10 w11 w12 w13 w14 w15 w16 ...... X p * *
				 * * * * * * * * * * * * * * * * X odd * * * * * * * * X p2 * * * * * * * * p3 *
				 * * * * * * * * p4 * * * * * * * *
				 */
				/*
				 * lecc=lecc>>1; lecc2=lecc2>>1;
				 */
			}
			long pos = lecc ^ lecc2;
			pos = pos >> 1;
			word[(int) (pos / 8)] ^= (1 << (pos % 8));

			if ((lecc & 0x1) != getParity(word))
				return null;
			return word;
		}
		return null;
	}

	/**
	 * compute ecc word w0 w1 w2 w3 w4 w5 w6 w7 w8 w9 w10 w11 w12 w13 w14 w15 w16
	 * ...... p * * * * * * * * * * * * * * * * * odd * * * * * * * * p2 * * * * * *
	 * * * p3 * * * * * * * * ecc.lec return ecc[0..n]=parity:odd:p2.... the size of
	 * word ecc is automatic, if word size is 2^n bits, ecc is n+2 bit size.
	 */
	public byte[] getECC(byte[] word) {
		if (word == null)
			return null;
		if (FAST_parity == null) {
			FAST_parity = new short[256];

			for (int i = 0; i < 256; i++) {
				FAST_parity[i] = (byte) (i ^ (i >> 1));
				// System.out.println("table0["+i+"]="+String.format("%2x", FAST_parity[i]));

			}
			for (int i = 0; i < 256; i++) {
				FAST_parity[i] = (byte) (FAST_parity[i] ^ (FAST_parity[i] >> 2));
				// System.out.println("table1["+i+"]="+String.format("%2x", FAST_parity[i]));
			}
			for (int i = 0; i < 256; i++) {
				FAST_parity[i] = (byte) (FAST_parity[i] ^ (FAST_parity[i] >> 4));
				// System.out.println("table2["+i+"]="+String.format("%2x", FAST_parity[i]));
			}
			for (int i = 0; i < 256; i++) {
				FAST_parity[i] = (byte) (FAST_parity[i] & 1);

				// System.out.println("table3["+i+"]="+String.format("%2x", FAST_parity[i]));
			} /*
				 * for(int i=0;i<256;i++) { FAST_parity[i]=(byte) (i& (1));
				 * System.out.println("table0["+i+"]="+String.format("%2x", FAST_parity[i]));
				 * 
				 * }
				 */
		}
		byte[] ecc = null;
		int lecc = 5;
		int lw = word.length;
		while ((lw = lw / 2) > 0)
			lecc++;
		lw = word.length;
		ecc = new byte[(lecc - 1) / 8 + 1];
		short byteb = 0;
		for (int i = 0; i < ecc.length; i++)
			ecc[i] = 0;
		for (int i = 0; i < lw; i++)
			byteb ^= word[i];

		ecc[0] = (byte) FAST_parity[byteb & 0xff];// P
		ecc[0] |= ((byteb >> 1 ^ byteb >> 3 ^ byteb >> 5 ^ byteb >> 7) & 1) << 1;// odd
		ecc[0] |= ((byteb >> 2 ^ byteb >> 3 ^ byteb >> 6 ^ byteb >> 7) & 1) << 2;// P2
		ecc[0] |= ((byteb >> 4 ^ byteb >> 5 ^ byteb >> 6 ^ byteb >> 7) & 1) << 3;// p3
		// System.out.println((short)(ecc[0]&0xff)+"\t"+"0123");
		/*
		 * for(int j=4;j<lecc;j++) for(int i=0;i<lw;i+=1<<(j-3))
		 * ecc[j/8]|=FAST_parity[(short)word[i]&0xff]<<(j%8);//4...
		 */
		/*
		 * X.X.X.X.X.X. ..XX..XX..XX ....XXXX.... ........XXXX
		 */
		long p = 0;
		for (int i = 0; i < lw; i++)
			p += FAST_parity[(short) word[i] & 0xff] << i;
		for (int j = 4; j < lecc - 1; j++)
			for (int i = 0; i < lw; i++) {
				// System.out.print((short)(ecc[j/8]&0xff)+"\t"+j+" "+i+" ");
				if ((i % (1 << (j - 3)) >= (1 << (j - 4)))) {
					// System.out.print("x");
					if (((p >> i) & 1) == 1) {
						ecc[j / 8] ^= (1 << j);
						// System.out.print("1");
					}

				} else
					ecc[j / 8] ^= 0;
				// System.out.println("");
			}
		return ecc;
	}

	public Ecc() {
		// TODO Auto-generated constructor stub
	}

}
