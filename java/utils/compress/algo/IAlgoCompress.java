package com.zoubworld.java.utils.compress.algo;

import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;

public interface IAlgoCompress {

	List<ISymbol> decodeSymbol(List<ISymbol> lenc);
	/*
	 * tab[0..255][0..255] list<offset> 65355 x
	 * 
	 * 
	 * dico1 256 50% dico2 65356 25% dico3 16777216 10%
	 * 
	 * 
	 * 1 Mo range; dico 16Mo 1 + 16*8+1.6*64=260Mo
	 * 
	 * 
	 */

	List<ISymbol> encodeSymbol(List<ISymbol> ldec);

}