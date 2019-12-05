package com.zoubworld.java.utils.compress;

import com.zoubworld.java.utils.compress.file.BinaryStdIn;

/** describe the coding rules between symbol and code
 * and translation of code in bit stream.
 * the id/value of a symbol is a class constant
 * the id/value of a code is real time define by algos
 * 
 * */
public interface ICodingRule {

	/**
	 * @param arg0
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	ICode get(ISymbol sym);

	ISymbol get(ICode code);

	/** read a code from bit stream
	 * */
	ICode getCode(BinaryStdIn binaryStdIn);
	/** read a symbol from bit stream
	 * */
	ISymbol getSymbol(BinaryStdIn binaryStdIn);

}