package com.zoubworld.java.utils.compress;

import java.util.Comparator;

public class CodeComparatorInteger implements Comparator<Code> {
	public int compare(Code s1, Code s2) {
		return s1.compareToInt(s2);

	}

}