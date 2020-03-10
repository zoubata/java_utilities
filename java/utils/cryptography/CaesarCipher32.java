/**
 * 
 */
package com.zoubworld.java.utils.cryptography;

/**
 * @author Pierre  Valleau
 *
 The Caesar cipher is a simple substitution cipher, which replaces each plaintext letter by a different letter of the alphabet. The cipher is named after Gaius Julius Caesar (100 BC – 44 BC), who used it for communication with his friends and allies.
Simple substitution cipher
Julius Caesar encrypted his correspondence in many ways, for example by writing texts in reverse order or writing Latin texts using Greek letters. Some ancient authors (for example, the Roman historian Gaius Suetonius Tranquillus, who lived in the first century of our era) wrote that he was using the cipher with various shifts, of one or three characters. 
in fact it is a 32bits cipher. because module is implicit, for a real 8 bits you have to change algo with 4 %256
info : http://www.crypto-it.net/eng/simple/index.html
 */
public class CaesarCipher32 implements ICryptoAlgo {

	/**
	 * 
	 */
	private CaesarCipher32() {
		// TODO Auto-generated constructor stub
	}
public CaesarCipher32(int mykey, int keysize) {
		buildkeys(mykey, keysize);
	}
int key=0;


	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.file.ICryptoAlgo#buildkeys(int, int)
	 * my key is an	int between 0 and 1^keysize-1 
	 * keysize : 1,2,4,8,16,32 .	 
	 */
	
	public void buildkeys(int mykey, int keysize)
	{key = 0;
		byte keyit = (byte) (32 / keysize);
		for (; keyit > 0; keyit--)
			key = (key << keysize) + mykey;
		}
	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.file.ICryptoAlgo#encrypt(int)
	 */
	@Override
	public Integer encrypt(int data)
	{
		return data = (int) ((data + key) & 0xFFFFFFFF);
	}
	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.file.ICryptoAlgo#decrypt(int)
	 */
	@Override
	public Integer decrypt(int data)
	{
		return data = (int) ((data - key) & 0xFFFFFFFF);
	}
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Integer insert() {
		// TODO Auto-generated method stub
		return null;
	}

}
