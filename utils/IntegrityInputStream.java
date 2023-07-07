/**
 * 
 */
package com.zoubworld.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Zoubata
 *
 */
public class IntegrityInputStream extends InputStream {

	InputStream in;
	MessageDigest digest;
	/**
	 * @throws NoSuchAlgorithmException 
	 * 
	 */
	public IntegrityInputStream(InputStream in,String algo) throws NoSuchAlgorithmException {
		this.in=in; 
		 digest = MessageDigest.getInstance(algo);//"SHA1"
         
	}

	@Override
	public int read() throws IOException {
	
         int b=in.read();
         if(b!=-1)
          digest.update((byte)b);  
		return b;
	}
	public  byte[] digest()
	{
		return digest.digest(); 
	}

}
