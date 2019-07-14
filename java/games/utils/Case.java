/**
 * 
 */
package com.zoubworld.java.games.utils;

/**
 * @author Pierre Valleau
 *
 */
public class Case {

	String value;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return value==null?"":value;
	//	return "Case [value=" + value + "]";
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 */
	public Case() {
		// TODO Auto-generated constructor stub
	}

	public Case(char c) {
		value=""+c;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
