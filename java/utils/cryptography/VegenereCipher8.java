package com.zoubworld.java.utils.cryptography;

/**
 * Vigenère cipher
 * The cipher was invented by Italian Giovan Battista Bellaso, who described it in 1553 in his book "La cifra del. Sig. Giovan Battista Bellaso". However it is named, due to the wrong widespread belief in the nineteenth century, after the French diplomat and alchemist Blaise de Vigenère, who lived in the sixteenth century.
 * The Vigenère cipher is quite easy to use and provide quite good security. It was widely used for a long time until the twentieth century. 
 info : http://www.crypto-it.net/eng/simple/index.html
 * */
public class VegenereCipher8 extends CaesarCipher8 {

	private VegenereCipher8(byte mykey) {
		super(mykey);
		// TODO Auto-generated constructor stub
	}
	public VegenereCipher8(String key) {
	super((byte)0);	
	this.key=key;	
	}
	String key;
	byte index=0;
	@Override
	public void reset() {
		index=0;		
	}
	public char getkey()
	{
		if( index>=key.length())
			index=0;
		return key.charAt(index);
	}
	@Override
	public byte encrypt(byte data)
	{
	byte c = (byte)((data + getkey())%256);
	index++;
	return c;
	}
	@Override
	public byte decrypt(byte data)
	{
	byte c = (byte)((data - getkey())%256);
	index++;
	return c;
	}
}
