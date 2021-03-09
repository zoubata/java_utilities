package com.zoubworld.java.utils.compress;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ISymbol extends Comparable<ISymbol> {

	boolean isChar();

	boolean isInt();

	boolean isShort();

	char getChar();

	Integer getInt();

	public Long getLong();

	short getShort();

	long getId();

	char[] toSymbol();

	String toString();

	ICode getCode();

	void setCode(ICode code2);

	/** List of class available to build ISymbol*/
	public static Class list[]= 
		{
			Symbol.class,
			Number.class,
				}; 
	
	/**
	 * from a list of symbol do the histogram of frequency
	 */
	public static Map<ISymbol, Long> Freq(List<ISymbol> l) {
		if (l==null) return null;
		return l.stream().parallel()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
	/** Factory that generate an ISymbol from it id and current sprout object
	 * */
	ISymbol Factory(Long nId);
}