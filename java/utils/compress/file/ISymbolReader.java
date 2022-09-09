package com.zoubworld.java.utils.compress.file;

import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;

public interface ISymbolReader {


/**
 * read a symbol from bit stream
 */
	public ISymbol getSymbol(IBinaryReader binaryStdIn);
	/** read the core of data structure for the coding rules definition.
	 */
	public ICodingRule ReadCodingRule(IBinaryReader binaryStdin);
}
