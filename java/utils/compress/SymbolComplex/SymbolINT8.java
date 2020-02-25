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
public class SymbolINT8 extends CompositeSymbol {

	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolINT8(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolINT8(ISymbol mys2) {
		super(Symbol.INT8, mys2);
		// TODO Auto-generated constructor stub
	}
	public SymbolINT8(char i) {
		super(Symbol.INT8, new Symbol(i));
		getS2().setCode(new Code(i,8));
	}
	public SymbolINT8(byte i) {
		super(Symbol.INT8, new Symbol(i));
		getS2().setCode(new Code(i));
	}
	public SymbolINT8(IBinaryReader binaryStdIn) {		
		super(Symbol.INT8, new Symbol( binaryStdIn.readInt(8)));
		this.getS2().setCode(new Code(this.getS2().getId(),8));		
	}

	@Override
	public String toString() {
		return (getS1().toString()+"("+getS2().getId()+")");
	}
}
