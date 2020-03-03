package com.zoubworld.java.utils.cryptography;

/**
 * The simple XOR cipher was quite popular in early times of computers, in operating systems MS-DOS and Macintosh.

Despite its simplicity and susceptibility to attacks, the cipher was used in many commercial applications, thanks to its speed and uncomplicated implementation. 
info : http://www.crypto-it.net/eng/simple/index.html
 * */
public class XorCipher8 extends CaesarCipher8 {

	private XorCipher8(byte mykey) {
		super(mykey);
		// TODO Auto-generated constructor stub
	}
	public XorCipher8(String key) {
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
		if( index>key.length())
			index=0;
		return key.charAt(index);
	}
	@Override
	public byte encrypt(byte data)
	{
	return (byte)((data ^ getkey()));
	}
	@Override
	public byte decrypt(byte data)
	{
	return (byte)((data ^ getkey()));
	}
}
