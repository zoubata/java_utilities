package com.zoubworld.java.utils.compress.SymbolComplex;

import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

public class SymbolINT extends CompositeSymbol {

	public SymbolINT(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
		
	}
	static public SymbolINT Factory(long i) {
		return Symbol.FactorySymbolINT(i);
		
	}

}
