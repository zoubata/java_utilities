/**
 * 
 */
package com.zoubworld.java.utils.cryptography;

/**
 * @author Pierre Valleau
 *
 *         The Caesar cipher is a simple substitution cipher, which replaces
 *         each plaintext letter by a different letter of the alphabet. The
 *         cipher is named after Gaius Julius Caesar (100 BC – 44 BC), who used
 *         it for communication with his friends and allies. Simple substitution
 *         cipher Julius Caesar encrypted his correspondence in many ways, for
 *         example by writing texts in reverse order or writing Latin texts
 *         using Greek letters. Some ancient authors (for example, the Roman
 *         historian Gaius Suetonius Tranquillus, who lived in the first century
 *         of our era) wrote that he was using the cipher with various shifts,
 *         of one or three characters. in fact it is a 8bits cipher. info :
 *         http://www.crypto-it.net/eng/simple/index.html
 */
public class CaesarCipher8 implements ICryptoAlgo {

	/**
	 * 
	 */
	private CaesarCipher8() {
		// TODO Auto-generated constructor stub
	}

	public CaesarCipher8(byte mykey) {
		key = mykey;
	}

	int key = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.ICryptoAlgo#encrypt(int)
	 */
	@Override
	public Integer encrypt(int data) {
		data = ((int) encrypt((byte) (data & 0xff)) & 0xff)
				| (((int) encrypt((byte) (((data >>> 8) & 0xff))) & 0xff) << 8)
				| (((int) encrypt((byte) (((data >>> 16) & 0xff))) & 0xff) << 16)
				| (((int) encrypt((byte) (((data >>> 24) & 0xff))) & 0xff) << 24);
		return data;
	}

	public byte encrypt(byte data) {
		return (byte) ((data + key) % 256);
	}

	public byte decrypt(byte data) {
		return (byte) ((data - key) % 256);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zoubworld.java.utils.compress.file.ICryptoAlgo#decrypt(int)
	 */
	@Override
	public Integer decrypt(int data) {
		data = (decrypt((byte) (data & 0xff)) & 0xff) | (((int) decrypt((byte) ((data >>> 8) & 0xff)) & 0xff) << 8)
				| (((int) decrypt((byte) ((data >>> 16) & 0xff)) & 0xff) << 16)
				| (((int) decrypt((byte) ((data >>> 24) & 0xff)) & 0xff) << 24);
		return data;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer insert() {
		return null;
	}

}
