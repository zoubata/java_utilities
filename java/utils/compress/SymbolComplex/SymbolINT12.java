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
 * 8 hours
 *
 */
public class SymbolINT12 extends CompositeSymbol {

	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolINT12(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
	}
	public SymbolINT12(ISymbol mys2) {
		super(Symbol.INT12, mys2);
	}
	public SymbolINT12(short i) {
		super(Symbol.INT12, new Symbol(i));
		getS2().setCode(new Code(i,12));
	}
	public SymbolINT12(BinaryStdIn binaryStdIn) {		
		super(Symbol.INT12, new Symbol( binaryStdIn.readInt(12)));
		this.getS2().setCode(new Code(this.getS2().getId(),12));		
	}
}