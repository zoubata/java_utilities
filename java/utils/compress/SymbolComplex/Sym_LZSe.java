package com.zoubworld.java.utils.compress.SymbolComplex;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.Symbol;

public class Sym_LZSe extends Sym_LZS {

	public Sym_LZSe(int offset, int length) {
		super(Symbol.LZSe, Symbol.FactorySymbolINT(offset),
				Symbol.FactorySymbolINT(length));// 16 bit number coding : INT12+0Bxxxxxxxxxxxxxxxx);
		
	}
	public long getOffset() {
		return Symbol.getINTn(getS1());
	}
	
	public long getLength() {
		return Symbol.getINTn(getS2());
	}
}
