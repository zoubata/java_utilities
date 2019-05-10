/**
 * 
 */
package com.zoubwolrd.java.utils.compress.SymbolComplex;

import com.zoubwolrd.java.utils.compress.Code;
import com.zoubwolrd.java.utils.compress.CompositeSymbol;
import com.zoubwolrd.java.utils.compress.ISymbol;
import com.zoubwolrd.java.utils.compress.Symbol;
import com.zoubwolrd.java.utils.compress.file.BinaryStdIn;

/**
 * @author zoubata
 *
 */
public class SymbolINT32 extends CompositeSymbol {

	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolINT32(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolINT32(ISymbol mys2) {
		super(Symbol.INT32, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolINT32(int i) {
		super(Symbol.INT32, new Symbol(i));
		getS2().setCode(new Code(i));
	}
	public SymbolINT32(BinaryStdIn binaryStdIn) {		
		super(Symbol.INT32, new Symbol( binaryStdIn.readInt(32)));
		this.getS2().setCode(new Code(this.getS2().getId(),32));		
	}
}
