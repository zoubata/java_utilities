/**
 * 
 */
package com.zoubworld.Crypto.server.account;

/**
 * @author zoubata
 *
 */
public class Wallet implements Iwallet {

	/**
	 * 
	 */
	public Wallet() {
		// TODO Auto-generated constructor stub
	}
	Key k=new Key();
	@Override
	public IKey getPublicKey() {
		// TODO Auto-generated method stub
		return k;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Iwallet w=new Wallet();
		
		
		System.out.println(w.getPublicKey());
		

	}

}
