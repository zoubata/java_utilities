package com.zoubworld.java.utils.compress;

import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;

/**
 * describe the coding rules between symbol and code and translation of code in
 * bit stream. the id/value of a symbol is a class constant the id/value of a
 * code is real time define by algos
 * 
 */
public interface ICodingRule {

	/**
	 * @param arg0
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	ICode get(ISymbol sym);

	ISymbol get(ICode code);

	/**
	 * read a code from bit stream (with additional data on it for complex code)
	 */
	ICode getCode(IBinaryReader binaryStdIn);

	/**
	 * read a code from bit stream without any additional data(s2)
	 */
	public ICode getGenericCode(IBinaryReader binaryStdIn);

	/**
	 * read a symbol from bit stream
	 */
	ISymbol getSymbol(IBinaryReader binaryStdIn);

	/**
	 * write the coding rules information (the coding table)
	 */
	void writeCodingRule(IBinaryWriter binaryStdOut);

	@Override
	public boolean equals(Object obj);

	/**
	 * read the coding rules information (the coding table) so read the Huffman tree
	 * based on coding rules of binaryStdIn
	 */
	static ICodingRule ReadCodingRule(IBinaryReader binaryStdin) {
		ISymbol sym = binaryStdin.readSymbol();

		if (sym == Symbol.HUFFMAN)

		{
			HuffmanCode h = new HuffmanCode();
			h.root = h.readTrie(binaryStdin);
			h.buildCode();

			return h;
		}
		if (CompositeCode.isit(sym)) {
			CompositeSymbol cs = (CompositeSymbol) sym;
			if (cs.getS1().equals(Symbol.INT12))

			{
				CompositeSymbol NbSym = (CompositeSymbol) sym;
				ISymbol symbitLen = binaryStdin.readSymbol();
				CompositeSymbol bitLen = (CompositeSymbol) symbitLen;

				CodingSet h = new CodingSet((int) NbSym.getS2().getId(), (int) bitLen.getS2().getId(), binaryStdin);

				return h;
			}
		}
		return null;
	}
}