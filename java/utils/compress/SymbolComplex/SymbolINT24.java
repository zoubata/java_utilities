/**
 * 
 */
package com.zoubworld.java.utils.compress.SymbolComplex;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.IBinaryReader;

/**
 * @author zoubata
 *
 */
public class SymbolINT24 extends SymbolINT {

	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolINT24(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
		// TODO Auto-generated constructor stub
	}

	public SymbolINT24(ISymbol mys2) {
		super(Symbol.INT24, mys2);
		// TODO Auto-generated constructor stub
	}

	public SymbolINT24(int i) {
		super(Symbol.INT24, new Symbol(i));
		getS1().setCode(new Code(i, 24));

	}

	public SymbolINT24(IBinaryReader binaryStdIn) {
		super(Symbol.INT24, new Symbol(binaryStdIn.readSignedInt(24)));
		this.getS1().setCode(new Code(this.getS1().getId(), 24));
	}

	@Override
	public String toString() {
		return (getS0().toString() + "(" + getS1().getId() + ")");
	}
}