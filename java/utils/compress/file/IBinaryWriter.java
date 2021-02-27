package com.zoubworld.java.utils.compress.file;

import java.util.List;

import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;

/**
 * @author Pierre Valleau
 *
 *         This interface is used to support write of any basic type of data on
 *         a storage object/file. byte, char,short,int, long string,
 *         float,double,Icode,Isymbol,n bits.
 *
 */

public interface IBinaryWriter {

	/**
	 * Flushes standard output, padding 0s if number of bits written so far is not a
	 * multiple of 8.
	 */
	void flush();

	/**
	 * Flushes and closes standard output. Once standard output is closed, you can
	 * no longer write bits to it.
	 */
	void close();

	/**
	 * Writes the specified bit to standard output.
	 * 
	 * @param x
	 *            the {@code boolean} to write.
	 */
	void write(boolean x);

	/**
	 * Writes the 8-bit byte to standard output.
	 * 
	 * @param x
	 *            the {@code byte} to write.
	 */
	void write(byte x);

	/**
	 * Writes the 32-bit int to standard output.
	 * 
	 * @param x
	 *            the {@code int} to write.
	 */
	void write(int x);

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
	void write(int x, int r);

	void write(long x, int r);

	/**
	 * Writes the 64-bit double to standard output.
	 * 
	 * @param x
	 *            the {@code double} to write.
	 */
	void write(double x);

	/**
	 * Writes the 64-bit long to standard output.
	 * 
	 * @param x
	 *            the {@code long} to write.
	 */
	void write(long x);

	/**
	 * Writes the 32-bit float to standard output.
	 * 
	 * @param x
	 *            the {@code float} to write.
	 */
	void write(float x);

	/**
	 * Writes the 16-bit int to standard output.
	 * 
	 * @param x
	 *            the {@code short} to write.
	 */
	void write(short x);

	/**
	 * Writes the 8-bit char to standard output.
	 * 
	 * @param x
	 *            the {@code char} to write.
	 * @throws IllegalArgumentException
	 *             if {@code x} is not betwen 0 and 255.
	 */
	void write(char x);

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
	void write(char x, int r);

	/**
	 * Writes the string of 8-bit characters to standard output.
	 * 
	 * @param s
	 *            the {@code String} to write.
	 * @throws IllegalArgumentException
	 *             if any character in the string is not between 0 and 255.
	 */
	void write(String s);

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
	void write(String s, int r);

	void write(ISymbol sym);

	void writes(List<ISymbol> ls);

	/**
	 * @return the codingRule
	 */
	ICodingRule getCodingRule();

	/**
	 * @param codingRule
	 *            the codingRule to set
	 */
	void setCodingRule(ICodingRule codingRule);

	void write(List<ICode> lc);

	void write(ICode code);

	void write(ICodingRule cs);
	/** jump in the bit Stream of nbBit
	 * */
	default void rjumpOut(long nbBit)
	{
		jumpOut(getposOut()+ nbBit);
	}
	void jumpOut(long nbBit);
	/** get the position on the stream.
	 * */
	Long getposOut();
}