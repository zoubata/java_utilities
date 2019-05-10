package com.zoubwolrd.java.utils.compress;

import java.util.Comparator;

public class MySymbolCodeComparator implements Comparator<ISymbol> {
	public int compare(ISymbol s1, ISymbol s2) {
		return s1.getCode().compareToCode(s2.getCode());

	}

}