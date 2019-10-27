package com.zoubworld.java.utils.compress;

public interface ISymbol extends Comparable<ISymbol>{

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

}