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
public class SymbolINT4 extends SymbolINT {

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
		getS1().setCode(new Code(i, 4));
	}

	public SymbolINT4(long i) {
		super(Symbol.INT4, new Symbol(i));
		getS1().setCode(new Code(i, 4));
	}

	public SymbolINT4(IBinaryReader binaryStdIn) {
		super(Symbol.INT4, new Symbol(binaryStdIn.readSignedInt(4)));
		
		this.getS1().setCode(new Code(this.getS1().getId(), 4));
	}

	

	@Override
	public String toString() {
		return (getS0().toString() + "(" + getS1().getId() + ")");
	}
}