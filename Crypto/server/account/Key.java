/**
 * 
 */
package com.zoubworld.Crypto.server.account;

import java.math.BigInteger;

/**
 * @author zoubata
 *
 */
public class Key implements IKey {

	BigInteger bikey=null;
	/**
	 * 
	 */
	public Key() {	
		bikey=BigInteger.valueOf((long)(Math.random()*Long.MAX_VALUE));
		for(int i=0;i<100;i++)
			bikey=bikey.multiply(BigInteger.valueOf((long)(Math.random()*Long.MAX_VALUE))).add(BigInteger.valueOf((long)(Math.random()*Integer.MAX_VALUE)));
		bikey=bikey.add(BigInteger.valueOf((long)(Math.random()*Integer.MAX_VALUE)));
	}
	public String toString()
	{
		return bikey.toString(16).toUpperCase();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Key k=new Key();
		System.out.println(k);

	}

}
