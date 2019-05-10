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
public class SymbolINT48 extends CompositeSymbol {

	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolINT48(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolINT48(ISymbol mys2) {
		super(Symbol.INT48, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolINT48(long i) {
		super(Symbol.INT48, new Symbol((long)i));
		getS2().setCode(new Code(i,48));
		
	}
	public SymbolINT48(BinaryStdIn binaryStdIn) {		
		super(Symbol.INT48, new Symbol( binaryStdIn.readLong(48)));
		this.getS2().setCode(new Code(this.getS2().getId(),48));		
	}
}
