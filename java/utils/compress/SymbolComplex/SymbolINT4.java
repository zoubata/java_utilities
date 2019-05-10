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
public class SymbolINT4 extends CompositeSymbol {

	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolINT4(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolINT4(ISymbol mys2) {
		super(Symbol.INT4, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolINT4(char i) {
		super(Symbol.INT4, new Symbol(i));
		getS2().setCode(new Code(i));
	}
	public SymbolINT4(byte i) {
		super(Symbol.INT4, new Symbol(i));
		getS2().setCode(new Code(i));
	}
	public SymbolINT4(BinaryStdIn binaryStdIn) {		
		super(Symbol.INT4, new Symbol( binaryStdIn.readInt(4)));
		this.getS2().setCode(new Code(this.getS2().getId(),4));		
	}
}