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
public class SymbolINT16 extends SymbolINT {

	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolINT16(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
	}

	public SymbolINT16(ISymbol mys2) {
		super(Symbol.INT16, mys2);
	}

	public SymbolINT16(short i) {
		super(Symbol.INT16, new Symbol(i));
		getS1().setCode(new Code(i, 16));
	}

	public SymbolINT16(IBinaryReader binaryStdIn) {
		super(Symbol.INT16, new Symbol(binaryStdIn.readSignedInt(16)));
		this.getS1().setCode(new Code(this.getS1().getId(), 16));
	}

	@Override
	public String toString() {
		return (getS0().toString() + "(" + getS1().getId() + ")");
	}
}
