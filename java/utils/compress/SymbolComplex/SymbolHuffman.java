package com.zoubworld.java.utils.compress.SymbolComplex;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;

/**
 * @author zoubata
 *
 */
public class SymbolHuffman extends CompositeSymbol {
HuffmanCode huff=null;
	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolHuffman(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolHuffman(ISymbol mys2) {
		super(Symbol.HUFFMAN, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolHuffman(int i) {
		super(Symbol.HUFFMAN, new Symbol(i));
		Code c=new Code();
		huff=HuffmanCode.getTables().get(i);
		huff.WriteTable(c);
		getS2().setCode(c);
	}
	public SymbolHuffman(BinaryStdIn binaryStdIn) {		
		super(Symbol.HUFFMAN, new Symbol( Integer.MAX_VALUE));
		huff=new HuffmanCode(binaryStdIn);
		Code c=new Code();
		huff.WriteTable(c);
		getS2().setCode(c);	}
}
