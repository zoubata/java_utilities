package com.zoubworld.java.utils.compress.algo;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.SymbolComplex.Sym_LZS;
import com.zoubworld.java.utils.compress.SymbolComplex.Sym_LZSe;

public class LZSe extends LZS {

	public LZSe() {
		// TODO Auto-generated constructor stub
	}
	public LZSe(int sizewindow,int MaxLen) {		
		this.sizewindow = sizewindow;	
this.MaxLen = MaxLen;
}
	@Override
	protected ISymbol buildsymbol(int pos, int size) {		
		return new Sym_LZSe(pos, size);
	}
	

	

}
