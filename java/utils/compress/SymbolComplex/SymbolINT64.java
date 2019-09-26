/**
 * 
 */
package com.zoubworld.java.utils.compress.SymbolComplex;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;

/**
 * @author zoubata
 * 
 * 
 *
 */
public class SymbolINT64 extends CompositeSymbol {

	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolINT64(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolINT64(ISymbol mys2) {
		super(Symbol.INT64, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolINT64(long i) {
		super(Symbol.INT64, new Symbol(i));
		getS2().setCode(new Code(i));
	}
	public SymbolINT64(BinaryStdIn binaryStdIn) {		
		super(Symbol.INT64, new Symbol( binaryStdIn.readLong(64)));
		this.getS2().setCode(new Code(this.getS2().getId(),64));		
	}

	@Override
	public String toString() {
		return (getS1().toString()+"("+getS2().getId()+")");
	}
}
