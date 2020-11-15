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
	public final static int iCodingSet = 0;
	public final static int iCodeNumberSet = 1;
	public final static int iHuffmanCode = 2;
	public final static int iShannonFanoEliasCode= 3;
	public final static int iSubSetSymbol = 4;
	public final static int iSubSetNumber = 5;
	

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
		ICodingRule h = null;
		if (sym == Symbol.HUFFMAN)

		{
			HuffmanCode h2 = new HuffmanCode();
			h2.root = h2.readTrie(binaryStdin);
			h2.buildCode();

			return h2;
		}
		if (sym == Symbol.CodingSet)

		{
			ICodingRule cr = binaryStdin.getCodingRule();
			binaryStdin.setCodingRule(new CodeNumberSet(CodeNumber.Golomb4Coding));
			ISymbol classNumber = binaryStdin.readSymbol();
			ISymbol configNumber = binaryStdin.readSymbol();

			switch ((int) classNumber.getId()) {
			case ICodingRule.iCodeNumberSet:
				h = new CodeNumberSet((int) configNumber.getId());
				break;
			case ICodingRule.iCodingSet:
				h = new CodingSet((int) configNumber.getId());
				break;
			case ICodingRule.iHuffmanCode:
				HuffmanCode h2 = new HuffmanCode();
				h2.root = h2.readTrie(binaryStdin);
				h2.buildCode();
				h = h2;
				break;
				

			case ICodingRule.iShannonFanoEliasCode:
				h = new ShannonFanoEliasCode( configNumber.getId(),binaryStdin);
				break;
			case ICodingRule.iSubSetSymbol:
				/** @todo to review and recode */
				h = (ICodingRule) new SubSetSymbol((int) configNumber.getId(),binaryStdin);
				break;
			case ICodingRule.iSubSetNumber:
				/** @todo to review and recode */
				h = (ICodingRule) new SubSetNumber((int) configNumber.getId(),binaryStdin);
				break;
			default:
				h=null;
				break;
			}
			binaryStdin.setCodingRule(cr);
			return h;
		}
		if (CompositeCode.isit(sym)) {
			CompositeSymbol cs = (CompositeSymbol) sym;
			if (cs.getS1().equals(Symbol.INT12))

			{
				CompositeSymbol NbSym = (CompositeSymbol) sym;
				ISymbol symbitLen = binaryStdin.readSymbol();
				CompositeSymbol bitLen = (CompositeSymbol) symbitLen;

				h = new CodingSet((int) NbSym.getS2().getId(), (int) bitLen.getS2().getId(), binaryStdin);

				return h;
			}
		}

		return null;
	}
}