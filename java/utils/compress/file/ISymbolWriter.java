package com.zoubworld.java.utils.compress.file;

import com.zoubworld.java.utils.compress.ISymbol;

public interface ISymbolWriter {
/**
 * write a symbol into bit stream
 */
	public void writeSymbol(IBinaryWriter binaryStdOut, ISymbol sym);
	/** write the core of data structure for the coding rules definition.
	 */
	public void writeCodingRule(IBinaryWriter binaryStdOut) ;
}