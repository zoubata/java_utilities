package com.zoubworld.java.utils.compress;

import java.io.FileOutputStream;
import java.io.IOException;

import com.zoubworld.java.utils.compress.file.IBinaryWriter;

public interface ICode {

	/**
	 * length of code in bits
	 */
	int length();

	ISymbol getSymbol();

	void setSymbol(ISymbol sym);

	/*
	 * a byte table that represent the code
	 */
	char[] toCode();

	/**
	 * bit string "010101" for 0x15(length()==6)
	 */
	String toRaw();

	/**
	 * return the bit from msb getMsb(0) is the msb
	 * 
	 * @param index
	 * @return
	 */
	Integer getMsb(int index);

	/**
	 * gray compare 0b1 smaller than 0b10 0b011 smaller then 0b1
	 */
	public int compareToCode(ICode s2);

	/**
	 * integer compare 0b1 bigger than 0b01 0b1 smaller than 0b10 0b011 bigger then
	 * 0b10 0b11 smaller then 0b100
	 * 
	 */
	public int compareToInt(ICode iCode);

	void write(IBinaryWriter o);

	/**
	 * return the bit stream as a long, starting at bit length()-1, and ending at
	 * bit 0.
	 * 
	 * @return the numeric value of bit stream
	 */
	Long getLong();

	void huffmanAddBit(char c);

	void write(FileOutputStream out) throws IOException;

}