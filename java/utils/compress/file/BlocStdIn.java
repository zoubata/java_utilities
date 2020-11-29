/**
 * 
 */
package com.zoubworld.java.utils.compress.file;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * @author pierre valleau
 * 
 *         a bloc is a n by n word memory area, and the word size is l byte. the
 *         block size is n*n*l bytes. uncompleted block is filled with 0
 *
 */
public class BlocStdIn {

	BufferedInputStream in;
	int n = 1;

	public byte[] readBlock() {
		byte[] buffer = new byte[n * n * l];
		try {

			int bytesRead = 0;
			bytesRead = in.read(buffer);
			if (bytesRead <= 0)
				return null;
			if (bytesRead != n * n * l) {
				for (int i = bytesRead; i < n * n * l; i++)
					buffer[i] = 0;
			}
		} catch (IOException e) {
			System.out.println("EOF");

		}
		return buffer;
	}

	byte l;

	/**
	 * n²:size of the block in word l: the word size in byte, usually 8
	 */

	public BlocStdIn(BufferedInputStream in, int n) {
		this.in = in;
		this.n = n;
		this.l = 8;
	}

	/**
	 * n²:size of the block in word l: the word size in byte, usually 8
	 */

	public BlocStdIn(BufferedInputStream in, int n, byte l) {
		this.in = in;
		this.n = n;
		this.l = (byte) l;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
