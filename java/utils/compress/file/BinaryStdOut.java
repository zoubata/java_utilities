package com.zoubworld.java.utils.compress.file;
/******************************************************************************
 *  Compilation:  javac BinaryStdOut.java
 *  Execution:    java BinaryStdOut
 *  Dependencies: none
 *
 *  Write binary data to standard output, either one 1-bit boolean,
 *  one 8-bit char, one 32-bit int, one 64-bit double, one 32-bit float,
 *  or one 64-bit long at a time.
 *
 *  The bytes written are not aligned.
 *
 ******************************************************************************/

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;

/**
 * <i>Binary standard output</i>. This class provides methods for converting
 * primtive type variables ({@code boolean}, {@code byte}, {@code char},
 * {@code int}, {@code long}, {@code float}, and {@code double}) to sequences of
 * bits and writing them to standard output. Uses big-endian (most-significant
 * byte first).
 * <p>
 * The client must {@code flush()} the output stream when finished writing bits.
 * <p>
 * The client should not intermix calls to {@code BinaryStdOut} with calls to
 * {@code StdOut} or {@code System.out}; otherwise unexpected behavior will
 * result.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class BinaryStdOut implements IBinaryWriter {
	private BufferedOutputStream out; // output stream (standard output)
	private int buffer; // 8-bit buffer of bits to write
	private int n; // number of bits remaining in buffer
	private boolean isInitialized; // has BinaryStdOut been called for first time?

	// don't instantiate
	public BinaryStdOut(PrintStream out2) {
		out = new BufferedOutputStream(out2);
	}

	public BinaryStdOut(File fout2) {
		try {
			out = new BufferedOutputStream(new FileOutputStream(fout2));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BinaryStdOut(String filename) {
		try {
			out = new BufferedOutputStream(new FileOutputStream(new File(filename)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BinaryStdOut() {
		out = new BufferedOutputStream(System.out);
	}

	// initialize BinaryStdOut
	private void initialize() {
		try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buffer = 0;
		n = 0;
		isInitialized = true;
	}

	/**
	 * Writes the specified bit to standard output.
	 */
	private void writeBit(boolean bit) {
		if (!isInitialized)
			initialize();

		// add bit to buffer
		buffer <<= 1;
		if (bit)
			buffer |= 1;

		// if buffer is full (8 bits), write out as a single byte
		n++;
		if (n == 8)
			clearBuffer();
	}

	/**
	 * Writes the 8-bit byte to standard output.
	 */
	private void writeByte(int x) {
		if (!isInitialized)
			initialize();

		assert x >= 0 && x < 256;

		// optimized if byte-aligned
		if (n == 0) {
			try {
				out.write(x);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		// otherwise write one bit at a time
		for (int i = 0; i < 8; i++) {
			boolean bit = ((x >>> (8 - i - 1)) & 1) == 1;
			writeBit(bit);
		}
	}

	// write out any remaining bits in buffer to standard output, padding with 0s
	private void clearBuffer() {
		if (!isInitialized)
			initialize();

		if (n == 0)
			return;
		if (n > 0)
			buffer <<= (8 - n);
		try {
			out.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		n = 0;
		buffer = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#flush()
	 */
	@Override
	public void flush() {
		clearBuffer();
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#close()
	 */
	@Override
	public void close() {
		flush();
		try {
			out.close();
			isInitialized = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(boolean)
	 */
	@Override
	public void write(boolean x) {
		writeBit(x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(byte)
	 */
	@Override
	public void write(byte x) {
		writeByte(x & 0xff);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(int)
	 */
	@Override
	public void write(int x) {
		writeByte((x >>> 24) & 0xff);
		writeByte((x >>> 16) & 0xff);
		writeByte((x >>> 8) & 0xff);
		writeByte((x >>> 0) & 0xff);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(int, int)
	 */
	@Override
	public void write(int x, int r) {
		if (r == 32) {
			write(x);
			return;
		}
		if (r < 1 || r > 32)
			throw new IllegalArgumentException("Illegal value for r = " + r);
		// if (x < 0 || x >= (1 << r)) throw new IllegalArgumentException("Illegal " + r
		// + "-bit char = " + x);
		for (int i = 0; i < r; i++) {
			boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
			writeBit(bit);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(long, int)
	 */
	@Override
	public void write(long x, int r) {
		if (r == 64) {
			write(x);
			return;
		}
		if (r == 32) {
			write((int) x);
			return;
		}

		if (r < 1 || r > 64)
			throw new IllegalArgumentException("Illegal value for r = " + r);
		/*
		 * if ( x > (1L << r)) throw new IllegalArgumentException("Illegal " + r +
		 * "-bit long = " + x); if (x < -(1L << r) ) throw new
		 * IllegalArgumentException("Illegal " + r + "-bit negatif long = " + x);
		 */ for (int i = 0; i < r; i++) {
			boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
			writeBit(bit);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(double)
	 */
	@Override
	public void write(double x) {
		write(Double.doubleToRawLongBits(x));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(long)
	 */
	@Override
	public void write(long x) {
		writeByte((int) ((x >>> 56) & 0xff));
		writeByte((int) ((x >>> 48) & 0xff));
		writeByte((int) ((x >>> 40) & 0xff));
		writeByte((int) ((x >>> 32) & 0xff));
		writeByte((int) ((x >>> 24) & 0xff));
		writeByte((int) ((x >>> 16) & 0xff));
		writeByte((int) ((x >>> 8) & 0xff));
		writeByte((int) ((x >>> 0) & 0xff));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(float)
	 */
	@Override
	public void write(float x) {
		write(Float.floatToRawIntBits(x));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(short)
	 */
	@Override
	public void write(short x) {
		writeByte((x >>> 8) & 0xff);
		writeByte((x >>> 0) & 0xff);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(char)
	 */
	@Override
	public void write(char x) {
		// if (x < 0 || x >= 65) throw new IllegalArgumentException("Illegal 8-bit char
		// = " + x);
		writeByte((x >> 8) & 0xff);
		writeByte(x & 0xff);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#write(char, int)
	 */
	@Override
	public void write(char x, int r) {
		if (r == 8) {
			write((byte) x);
			return;
		}
		if (r < 1 || r > 16)
			throw new IllegalArgumentException("Illegal value for r = " + r);
		if (x >= (1 << r))
			throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
		for (int i = 0; i < r; i++) {
			boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
			writeBit(bit);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.file.IBinaryWriter#write(java.lang.String)
	 */
	@Override
	public void write(String s) {
		for (int i = 0; i < s.length(); i++)
			write((byte) s.charAt(i));
		write((byte) 0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.file.IBinaryWriter#write(java.lang.String,
	 * int)
	 */
	@Override
	public void write(String s, int r) {
		for (int i = 0; i < s.length(); i++)
			write((byte) s.charAt(i), r);
		write((byte) 0, r);

	}

	/**
	 * Tests the methods in this class.
	 *
	 * @param args
	 *            the command-line arguments
	 */
	public static void main(String[] args) {
		/*int m = Integer.parseInt(args[0]);
		IBinaryWriter o = new BinaryStdOut();
		// write n integers to binary standard output
		for (int i = 0; i < m; i++) {
			o.write(i);
		}
		o.flush();*/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.file.IBinaryWriter#write(com.zoubworld.java
	 * .utils.compress.ISymbol)
	 */
	@Override
	public void write(ISymbol sym) {
		if (sym == null)
			return;
		if (codingRule == null)
			write(sym.getCode());
		else
			write(codingRule.get(sym));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.file.IBinaryWriter#writes(java.util.List)
	 */
	@Override
	public void writes(List<ISymbol> ls) {
		if (ls == null)
			return;
		if (codingRule == null)
			for (ISymbol sym : ls)
				write(sym.getCode());
		else
			for (ISymbol sym : ls)
				write(codingRule.get(sym));

	}

	ICodingRule codingRule = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#getCodingRule()
	 */
	@Override
	public ICodingRule getCodingRule() {
		return codingRule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.IBinaryWriter#setCodingRule(com.
	 * zoubworld.java.utils.compress.ICodingRule)
	 */
	@Override
	public void setCodingRule(ICodingRule codingRule) {
		this.codingRule = codingRule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.file.IBinaryWriter#write(java.util.List)
	 */
	@Override
	public void write(List<ICode> lc) {
		if (lc == null)
			return;
		for (ICode c : lc)
			write(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.file.IBinaryWriter#write(com.zoubworld.java
	 * .utils.compress.ICode)
	 */
	@Override
	public void write(ICode code) {
		if (code == null)
			return;
		if (code.length() <= 64)
			write(code.getLong(), code.length());
		else
			new Exception("code too long");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.file.IBinaryWriter#write(com.zoubworld.java
	 * .utils.compress.ICodingRule)
	 */
	@Override
	public void write(ICodingRule cs) {

		cs.writeCodingRule(this);

	}

}
