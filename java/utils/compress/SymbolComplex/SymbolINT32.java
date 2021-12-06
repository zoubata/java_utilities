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
public class SymbolINT32 extends SymbolINT {

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
		getS2().setCode(new Code(i, 32));
	}

	public SymbolINT32(IBinaryReader binaryStdIn) {
		super(Symbol.INT32, new Symbol(binaryStdIn.readInt()));
		this.getS2().setCode(new Code(this.getS2().getId(), 32));
	}

	@Override
	public String toString() {
		return (getS1().toString() + "(" + getS2().getId() + ")");
	}
}
