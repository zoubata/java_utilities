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
public class SymbolINT48 extends SymbolINT {

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
		super(Symbol.INT48, new Symbol((long) i));
		getS2().setCode(new Code(i, 48));

	}

	public SymbolINT48(IBinaryReader binaryStdIn) {
		super(Symbol.INT48, new Symbol(binaryStdIn.readSignedLong(48)));
		this.getS2().setCode(new Code(this.getS2().getId(), 48));
	}

	@Override
	public String toString() {
		return (getS1().toString() + "(" + getS2().getId() + ")");
	}
}
