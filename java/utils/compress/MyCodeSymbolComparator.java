package com.zoubwolrd.java.utils.compress;

import java.util.Comparator;

public class MyCodeSymbolComparator implements Comparator<ISymbol> {
	public int compare(ISymbol s1, ISymbol s2) {
		return s1.getCode().compareToInt(s2.getCode());

	}}