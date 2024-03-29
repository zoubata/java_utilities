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
 * @author zoubata 8 hours
 *
 */
public class SymbolINT12 extends SymbolINT {

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

	@Override
	public String toString() {
		return (getS0().toString() + "(" + getS1().getId() + ")");
	}

	public SymbolINT12(short i) {
		super(Symbol.INT12, new Symbol(i));
		getS1().setCode(new Code(i, 12));
	}

	public SymbolINT12(IBinaryReader binaryStdIn) {
		super(Symbol.INT12, new Symbol(binaryStdIn.readSignedInt(12)));
		this.getS1().setCode(new Code(this.getS1().getId(), 12));
	}
}