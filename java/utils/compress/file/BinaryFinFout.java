/**
 * 
 */
package com.zoubworld.java.utils.compress.file;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;

/**
 * @author Pierre Valleau
 *
 *         this class implement a fifo that manage bits.
 *
 */
public class BinaryFinFout implements IBinaryReader, IBinaryWriter {
	protected List<Integer> fifodata;
	protected long bufferout; // one character buffer
	protected int indexOut; // number of bits left in buffer

	protected boolean isInitialized; // has BinaryStdIn been called for first time?

	/**
	 * 
	 */
	public BinaryFinFout() {
		initialize();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	protected final int EOF = -1; // end of file

	protected long bufferin; // one character buffer
	protected int indexIn; // number of bits left in buffer

	/**
	 * return the size on bit
	 */
	public long size() {
		if (fifodata == null)
			return 0;
		if(indexlist!=0)
		{fifodata=fifodata.subList(indexlist, fifodata.size());indexlist=0;}

		return fifodata.size() * 32 + indexOut + indexIn;
	}

	// fill buffer
	protected void initialize() {
		bufferin = 0;
		bufferout = 0;
		indexOut = 0;
		indexIn = 0;
		// fillBuffer();
		isInitialized = true;
		fifodata = new ArrayList<Integer>();
	}

	int indexlist=0;
	protected void fillBuffer() {
		if (fifodata == null)
			return;
		if (((!fifodata.isEmpty()&&(indexlist<fifodata.size())) || indexIn != 0)) 
		{
			
		//if (!fifodata.isEmpty() || indexIn != 0) {
			if (indexIn == 0) {
				//bufferin = fifodata.remove(0);
				bufferin = fifodata.get(indexlist);indexlist++;
				
				indexIn = 32;
			} else if (!fifodata.isEmpty()) {
				bufferin <<= 32;
				bufferin = bufferin | ((long) ( fifodata.get(indexlist++)) & 0xFFFFFFFFL);
				indexIn += 32;
			} else
				throw new NoSuchElementException("Reading from empty input stream");

		} else {
			bufferin = EOF;
			indexIn = -2;
			throw new NoSuchElementException("Reading from empty input stream");

		}
		if(indexlist>1000)
			{fifodata=fifodata.subList(indexlist, fifodata.size());indexlist=0;}

	}

	/**
	 * Close this input stream and release any associated system resources.
	 */
	public void close() {
		if (!isInitialized)
			initialize();
		bufferin = 0;
		bufferout = 0;
		indexOut = 0;
		indexIn = 0;
		isInitialized = false;
		fifodata.clear();

	}

	/**
	 * Returns true if standard input is empty.
	 * 
	 * @return true if and only if standard input is empty
	 */
	public boolean isEmpty() {
		if (!isInitialized)
			initialize();
		return fifodata.isEmpty() && indexIn <= 0;
	}

	/**
	 * Reads the next bit of data from standard input and return as a boolean.
	 *
	 * @return the next bit of data from standard input as a {@code boolean}
	 * @throws NoSuchElementException
	 *             if standard input is empty
	 */
	public Boolean readBoolean() {
		try{
			if (indexIn == 0)
			{
		if (isEmpty())
			return null;// throw new NoSuchElementException("Reading from empty input stream");
		
			fillBuffer();
		}
			}
		catch(Exception e) {
			  return null;
		}
		indexIn--;
		boolean bit = ((bufferin >> indexIn) & 1) == 1;
		return bit;
	}

	/**
	 * Reads the next 8 bits from standard input and return as an 8-bit char. Note
	 * that {@code char} is a 16-bit type; to read the next 16 bits as a char, use
	 * {@code readChar(16)}.
	 *
	 * @return the next 8 bits of data from standard input as a {@code char}
	 * @throws NoSuchElementException
	 *             if there are fewer than 8 bits available on standard input
	 */
	public char readChar() {
		if (isEmpty())
			throw new NoSuchElementException("Reading from empty input stream");

		if (indexIn < 16)
			fillBuffer();

		char x;
		{
			indexIn -= 16;
			x = (char) ((long) (bufferin >> indexIn) & 0xffffL);
		}
		/*
		 * else { // combine last n bits of current buffer with first 8-n bits of new
		 * buffer x =(int) bufferin; x <<= (8 - indexIn); int oldN = indexIn;
		 * fillBuffer(); if (isEmpty()) throw new
		 * NoSuchElementException("Reading from empty input stream"); indexIn += oldN-8;
		 * x |= (bufferin >>> indexIn); }
		 */
		return x;
		// the above code doesn't quite work for the last character if n = 8
		// because buffer will be -1, so there is a special case for aligned byte
	}

	public ISymbol readSymbol() {
		if (codingRule != null)
			return codingRule.getSymbol(this);
		return null;
	}

	public ICode readCode() {
		ICode c = null;
		if (codingRule != null)
			return codingRule.getCode(this);
		return c;
	}

	public ISymbol readSymbol(HuffmanCode huff) {
		return huff.decodeASymbol(this);
	}

	/**
	 * Reads the next r bits from standard input and return as an r-bit character.
	 *
	 * @param r
	 *            number of bits to read.
	 * @return the next r bits of data from standard input as a {@code char}
	 * @throws NoSuchElementException
	 *             if there are fewer than {@code r} bits available on standard
	 *             input
	 * @throws IllegalArgumentException
	 *             unless {@code 1 <= r <= 16}
	 */
	public char readChar(int r) {
		if (r < 1 || r > 16)
			throw new IllegalArgumentException("Illegal value of r = " + r);

		char x = 0;
		for (int i = 0; i < r; i++) {
			x <<= 1;
			boolean bit = readBoolean();
			if (bit)
				x |= 1;
		}
		return x;
	}

	ICodingRule codingRule = null;

	/**
	 * @return the codingRule
	 */
	public ICodingRule getCodingRule() {
		return codingRule;
	}

	/**
	 * @param codingRule
	 *            the codingRule to set
	 */
	public void setCodingRule(ICodingRule codingRule) {
		this.codingRule = codingRule;
	}

	/**
	 * Reads the remaining bytes of data from standard input and return as a string.
	 *
	 * @return the remaining bytes of data from standard input as a {@code String}
	 * @throws NoSuchElementException
	 *             if standard input is empty or if the number of bits available on
	 *             standard input is not a multiple of 8 (byte-aligned)
	 */
	public String readString() {
		if (isEmpty())
			throw new NoSuchElementException("Reading from empty input stream");

		StringBuilder sb = new StringBuilder();
		char c = (char) readByte();
		while (c != 0) {

			sb.append(c);
			if (isEmpty())
				c = 0;
			else
				c = (char) readByte();
		}
		return sb.toString();
	}

	public String readString(int i) {
		StringBuilder sb = new StringBuilder();
		char c = (char) readByte();
		while (i-- > 0 && !isEmpty()) {

			sb.append(c);
			c = (char) readByte();
		}
		sb.append(c);
		return sb.toString();
	}

	/**
	 * Reads the next 16 bits from standard input and return as a 16-bit short.
	 *
	 * @return the next 16 bits of data from standard input as a {@code short}
	 * @throws NoSuchElementException
	 *             if there are fewer than 16 bits available on standard input
	 */
	public short readShort() {
		short x = (short) readChar();

		return x;
	}

	/**
	 * Reads the next 32 bits from standard input and return as a 32-bit int.
	 *
	 * @return the next 32 bits of data from standard input as a {@code int}
	 * @throws NoSuchElementException
	 *             if there are fewer than 32 bits available on standard input
	 */
	public int readInt() {
		int x = 0;
		for (int i = 0; i < 4; i++) {
			int c = readByte() & 0xff;
			x <<= 8;

			x |= c;
		}
		return x;
	}

	/**
	 * Reads the next r bits from standard input and return as an r-bit int.
	 *
	 * @param r
	 *            number of bits to read.
	 * @return the next r bits of data from standard input as a {@code int}
	 * @throws NoSuchElementException
	 *             if there are fewer than {@code r} bits available on standard
	 *             input
	 * @throws IllegalArgumentException
	 *             unless {@code 1 <= r <= 32}
	 */
	public Integer readSignedInt(int r) {
		if (r < 1 || r > 32)
			throw new IllegalArgumentException("Illegal value of r = " + r);

		// optimize r = 32 case
		if (r == 32)
			return readInt();
		if (r == 16)
			return (int) readShort();
		if (r == 8)
			return (int) readByte();

		int x = 0;
		for (int i = 0; i < r; i++) {
			x <<= 1;
			Boolean bit = readBoolean();
			if (bit == null)
				return null;
			if (bit)
				x |= 1;
		}
		return (int)signed(x,r);
		
	}
	
	public Integer readUnsignedInt(int r) {
		if (r < 1 || r > 32)
			throw new IllegalArgumentException("Illegal value of r = " + r);

		int x = 0;
		for (int i = 0; i < r; i++) {
			x <<= 1;
			Boolean bit = readBoolean();
			if (bit == null)
				return null;
			if (bit)
				x |= 1;
		}
		return (int)x;
		
	}

	/**
	 * Reads the next r bits from standard input and return as an r-bit int.
	 *
	 * @param r
	 *            number of bits to read.
	 * @return the next r bits of data from standard input as a {@code int}
	 * @throws NoSuchElementException
	 *             if there are fewer than {@code r} bits available on standard
	 *             input
	 * @throws IllegalArgumentException
	 *             unless {@code 1 <= r <= 32}
	 */
	public long readSignedLong(int r) {
		if (r < 1 || r > 64)
			throw new IllegalArgumentException("Illegal value of r = " + r);

		// optimize r = 32 case
		if (r == 32) {
			long l = readInt();			
			return l;
		}
		if (r == 64)
			return readLong();

		long x = 0;
		for (int i = 0; i < r; i++) {
			x <<= 1;
			boolean bit = readBoolean();
			if (bit)
				x |= 1;
		}
		return  signed(x,r);
	}
	public Long readUnsignedLong(int r) {
		if (r < 1 || r > 64)
			throw new IllegalArgumentException("Illegal value of r = " + r);

		

		long x = 0;
		for (int i = 0; i < r; i++) {
			x <<= 1;
			boolean bit = readBoolean();
			if (bit)
				x |= 1;
		}
		return  x;
	}

	public long readLong(int len, boolean bigendian) {
		long s = readSignedLong(len);
		if (bigendian)
			return s;
		if (len % 8 == 0) {
			long sr = 0;
			for (int i = 0; i < len; i += 8) {
				long tmp = ((s >> i) & 0xff);
				tmp = tmp << (len - i - 8);
				sr |= tmp;
			}
			return sr;

		}
		return s;
	}

	/**
	 * Reads the next 64 bits from standard input and return as a 64-bit long.
	 *
	 * @return the next 64 bits of data from standard input as a {@code long}
	 * @throws NoSuchElementException
	 *             if there are fewer than 64 bits available on standard input
	 */
	public long readLong() {
		long x = 0;
		for (int i = 0; i < 8; i++) {
			long c = readByte() & 0xffL;
			x <<= 8;
			x |= c;
		}
		return x;
	}

	/**
	 * Reads the next 64 bits from standard input and return as a 64-bit double.
	 *
	 * @return the next 64 bits of data from standard input as a {@code double}
	 * @throws NoSuchElementException
	 *             if there are fewer than 64 bits available on standard input
	 */
	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	/**
	 * Reads the next 32 bits from standard input and return as a 32-bit float.
	 *
	 * @return the next 32 bits of data from standard input as a {@code float}
	 * @throws NoSuchElementException
	 *             if there are fewer than 32 bits available on standard input
	 */
	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	/**
	 * Reads the next 8 bits from standard input and return as an 8-bit byte.
	 *
	 * @return the next 8 bits of data from standard input as a {@code byte}
	 * @throws NoSuchElementException
	 *             if there are fewer than 8 bits available on standard input
	 */
	public byte readByte() {
		if (indexIn < 8)
			fillBuffer();

		byte x;
		{
			indexIn -= 8;
			x = (byte) (((long) (bufferin >> indexIn)) & 0xffL);
		}
		return x;
	}

	/** read all symbols */
	public List<ISymbol> readSymbols() {
		List<ISymbol> ls = new ArrayList<ISymbol>((int)(this.size()/8));
		ISymbol e = null;
		while ((e = readSymbol()) != null)
			ls.add(e);
		return ls;
	}

	/** read symbols up to n */
	public List<ISymbol> readSymbols(int n) {
		List<ISymbol> ls = new ArrayList<ISymbol>();
		ISymbol e = null;
		while ((n-- > 0) && ((e = readSymbol()) != null))
			ls.add(e);
		return ls;
	}

	/**
	 * Writes the specified bit to standard output.
	 */
	private void writeBit(boolean bit) {
		if (!isInitialized)
			initialize();

		// add bit to buffer
		bufferout <<= 1;
		if (bit)
			bufferout |= 1;
		indexOut++;

		// if buffer is full (8 bits), write out as a single byte
		if (indexOut == 32) {
			fifodata.add((int) (bufferout & 0xffffffff));
			indexOut = 0;
			bufferout = 0;
		} else if (indexOut > 32) {
			fifodata.add((int) (bufferout & 0xffffffff));
			indexOut -= 32;
			bufferout >>= 32;
		}
	}

	/**
	 * Writes the 8-bit byte to standard output.
	 */
	private void writeByte(int x) {
		if (!isInitialized)
			initialize();

		assert x >= 0 && x < 256;

		{
			bufferout <<= 8;
			bufferout |= x & 0xff;
			indexOut += 8;
		}

		updateBufferout();
	}

	private void clearBuffer() {
		clearBufferout();
		// clearBufferin();
	}

	protected void updateBufferout() {
		if (indexOut == 32) {
			fifodata.add((int) (bufferout & 0xffffffff));
			indexOut = 0;
			bufferout = 0;
		} else if (indexOut > 32) {
			indexOut -= 32;
			int x = ((int) bufferout & ((1 << indexOut) - 1));
			fifodata.add((int) ((bufferout >> indexOut) & 0xffffffff));
			bufferout = x;
		}
	}

	// write out any remaining bits in buffer to standard output, padding with 0s
	private void clearBufferout() {
		if (!isInitialized)
			initialize();

		if (indexOut == 0)
			return;
		if (indexOut > 0)
			bufferout <<= (32 - indexOut);
		fifodata.add((int) (bufferout & 0xffffffff));
		indexOut = 0;
		bufferout = 0;
	}

	/**
	 * Flushes standard output, padding 0s if number of bits written so far is not a
	 * multiple of 8.
	 */
	public void flush() {
		clearBuffer();

	}

	/**
	 * Writes the specified bit to standard output.
	 * 
	 * @param x
	 *            the {@code boolean} to write.
	 */
	public void write(boolean x) {
		writeBit(x);
	}

	/**
	 * Writes the 8-bit byte to standard output.
	 * 
	 * @param x
	 *            the {@code byte} to write.
	 */
	public void write(byte x) {
		writeByte(x & 0xff);
	}
	public void write(byte b[]) {		
		for(byte x:b)
		writeByte(x&0xff);
	}
	/**
	 * Writes the 32-bit int to standard output.
	 * 
	 * @param x
	 *            the {@code int} to write.
	 */
	public void write(int x) {
		writeByte((x >>> 24) & 0xff);
		writeByte((x >>> 16) & 0xff);
		writeByte((x >>> 8) & 0xff);
		writeByte((x >>> 0) & 0xff);
	}

	/**
	 * Writes the r-bit int to standard output.
	 * 
	 * @param x
	 *            the {@code int} to write.
	 * @param r
	 *            the number of relevant bits in the char.
	 * @throws IllegalArgumentException
	 *             if {@code r} is not between 1 and 32.
	 * @throws IllegalArgumentException
	 *             if {@code x} is not between 0 and 2<sup>r</sup> - 1.
	 */
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

	/**
	 * Writes the 64-bit double to standard output.
	 * 
	 * @param x
	 *            the {@code double} to write.
	 */
	public void write(double x) {
		write(Double.doubleToRawLongBits(x));
	}

	/**
	 * Writes the 64-bit long to standard output.
	 * 
	 * @param x
	 *            the {@code long} to write.
	 */
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

	/**
	 * Writes the 32-bit float to standard output.
	 * 
	 * @param x
	 *            the {@code float} to write.
	 */
	public void write(float x) {
		write(Float.floatToRawIntBits(x));
	}

	/**
	 * Writes the 16-bit int to standard output.
	 * 
	 * @param x
	 *            the {@code short} to write.
	 */
	public void write(short x) {
		writeByte((x >>> 8) & 0xff);
		writeByte((x >>> 0) & 0xff);
	}

	/**
	 * Writes the 8-bit char to standard output.
	 * 
	 * @param x
	 *            the {@code char} to write.
	 * @throws IllegalArgumentException
	 *             if {@code x} is not betwen 0 and 255.
	 */
	public void write(char x) {
		if (x < 0 || x >= 65535)
			throw new IllegalArgumentException("Illegal 8-bit char = " + x);
		writeByte((x >>> 8) & 0xff);
		writeByte((x >>> 0) & 0xff);
	}

	/**
	 * Writes the r-bit char to standard output.
	 * 
	 * @param x
	 *            the {@code char} to write.
	 * @param r
	 *            the number of relevant bits in the char.
	 * @throws IllegalArgumentException
	 *             if {@code r} is not between 1 and 16.
	 * @throws IllegalArgumentException
	 *             if {@code x} is not between 0 and 2<sup>r</sup> - 1.
	 */
	public void write(char x, int r) {
		if (r == 8) {
			write((byte)x);
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

	/**
	 * Writes the string of 8-bit characters to standard output.
	 * 
	 * @param s
	 *            the {@code String} to write.
	 * @throws IllegalArgumentException
	 *             if any character in the string is not between 0 and 255.
	 */
	public void write(String s) {
		for (int i = 0; i < s.length(); i++)
			write((byte) s.charAt(i));
		write((byte) 0);

	}

	/**
	 * Writes the string of r-bit characters to standard output.
	 * 
	 * @param s
	 *            the {@code String} to write.
	 * @param r
	 *            the number of relevants bits in each character.
	 * @throws IllegalArgumentException
	 *             if r is not between 1 and 16.
	 * @throws IllegalArgumentException
	 *             if any character in the string is not between 0 and 2<sup>r</sup>
	 *             - 1.
	 */
	public void write(String s, int r) {
		for (int i = 0; i < s.length(); i++)
			write((byte)s.charAt(i), r);
		write((byte) 0, r);

	}

	public void write(ISymbol sym) {
		if (sym == null)
			return;
		if (codingRule == null)
			write(sym.getCode());
		else
			write(codingRule.get(sym));

	}

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

	public void write(List<ICode> lc) {
		if (lc == null)
			return;
		for (ICode c : lc)
			write(c);
	}

	public void write(ICode code) {
		if (code == null)
			return;
		code.write(this);
		/*
		 * if(code.length()<=64) write(code.getLong(),code.length()); else new
		 * Exception("code too long");
		 */
	}

	public void write(ICodingRule cs) {

		cs.writeCodingRule(this);

	}

	@Override
	public byte[] readBytes(int l) {
		byte[] ba=new byte[l] ;
		for(int i=0;i<l;i++)
			ba[i]=readByte();
		return ba;
	}
	
	

	/** fix the signe of a int
	 * */
	protected static long signed(long readInt, int i) {
		
		if (((readInt>>>(i-1))&1)==1)
			return (-(1<<i))|readInt;
		return readInt;
	}

	
}
