/**
 * 
 */
package com.zoubworld.utils;

import com.atmel.pe.utils.IStringFormater;

/**
 * @author pvalleau
 *
 */
public abstract class StringFormater implements IStringFormater {

	/* (non-Javadoc)
	 * @see com.atmel.pe.utils.IStringFormater#convert(java.lang.String)
	 */
	@Override
	public abstract String convert(String value);
	/**
	 * 
	 */
	public StringFormater() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
