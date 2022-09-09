package com.zoubworld.java.utils.compress.file;

import java.util.List;
import java.util.NoSuchElementException;

import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.binalgo.HuffmanCode;

/**
 * @author Pierre Valleau
 *
 *         This interface is used to support read of any basic type of data on a
 *         storage object/file. byte, char,short,int, long string,
 *         float,double,Icode,Isymbol,n bits.
 *
 */
public interface IBinaryReader {

	/**
	 * Close this input stream and release any associated system resources.
	 */
	void close();

	/**
	 * Returns true if standard input is empty.
	 * 
	 * @return true if and only if standard input is empty
	 */
	boolean isEmpty();

	/**
	 * Reads the next bit of data from standard input and return as a boolean.
	 *
	 * @return the next bit of data from standard input as a {@code boolean}
	 * @throws NoSuchElementException
	 *             if standard input is empty
	 */
	Boolean readBoolean();

	/**
	 * Reads the next 8 bits from standard input and return as an 8-bit char. Note
	 * that {@code char} is a 16-bit type; to read the next 16 bits as a char, use
	 * {@code readChar(16)}.
	 *
	 * @return the next 8 bits of data from standard input as a {@code char}
	 * @throws NoSuchElementException
	 *             if there are fewer than 8 bits available on standard input
	 */
	char readChar();

	ISymbol readSymbol();

	ICode readCode();

	ISymbol readSymbol(HuffmanCode huff);

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
	char readChar(int r);

	/**
	 * @return the codingRule
	 */
	ICodingRule getCodingRule();

	/**
	 * @param codingRule
	 *            the codingRule to set
	 */
	void setCodingRule(ICodingRule codingRule);

	/**
	 * Reads the remaining bytes of data from standard input and return as a string.
	 *
	 * @return the remaining bytes of data from standard input as a {@code String}
	 * @throws NoSuchElementException
	 *             if standard input is empty or if the number of bits available on
	 *             standard input is not a multiple of 8 (byte-aligned)
	 */
	String readString();

	/**
	 * Reads the next 16 bits from standard input and return as a 16-bit short.
	 *
	 * @return the next 16 bits of data from standard input as a {@code short}
	 * @throws NoSuchElementException
	 *             if there are fewer than 16 bits available on standard input
	 */
	short readShort();

	/**
	 * Reads the next 32 bits from standard input and return as a 32-bit int.
	 *
	 * @return the next 32 bits of data from standard input as a {@code int}
	 * @throws NoSuchElementException
	 *             if there are fewer than 32 bits available on standard input
	 */
	int readInt();

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
	Integer readSignedInt(int r);
	
	//Integer readSignedInt(int r);

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
	long readSignedLong(int r);
//	Long readSignedLong(int i);


	long readLong(int r, boolean bigendian);

	/**
	 * Reads the next 64 bits from standard input and return as a 64-bit long.
	 *
	 * @return the next 64 bits of data from standard input as a {@code long}
	 * @throws NoSuchElementException
	 *             if there are fewer than 64 bits available on standard input
	 */
	long readLong();

	/**
	 * Reads the next 64 bits from standard input and return as a 64-bit double.
	 *
	 * @return the next 64 bits of data from standard input as a {@code double}
	 * @throws NoSuchElementException
	 *             if there are fewer than 64 bits available on standard input
	 */
	double readDouble();

	/**
	 * Reads the next 32 bits from standard input and return as a 32-bit float.
	 *
	 * @return the next 32 bits of data from standard input as a {@code float}
	 * @throws NoSuchElementException
	 *             if there are fewer than 32 bits available on standard input
	 */
	float readFloat();

	/**
	 * Reads the next 8 bits from standard input and return as an 8-bit byte.
	 *
	 * @return the next 8 bits of data from standard input as a {@code byte}
	 * @throws NoSuchElementException
	 *             if there are fewer than 8 bits available on standard input
	 */
	byte readByte();
	/** read a array of byte of len l
	 * */
	byte[] readBytes(int l);

	/** read all symbols */
	List<ISymbol> readSymbols();

	/** read symbols up to n */
	List<ISymbol> readSymbols(int n);

	Integer readUnsignedInt(int len);

	Long readUnsignedLong(int i);

	
}