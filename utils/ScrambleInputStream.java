/**
 * 
 */
package com.zoubworld.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.math3.random.ISAACRandom;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * @author Zoubata
 *
 */
public class ScrambleInputStream extends InputStream {

	InputStream file;
	RandomGenerator rng = null;
	 SecureRandom secureRandom= null;
	byte nonce[]=null;
	/**
	 * 
	 */
	public ScrambleInputStream(InputStream a) {
		
		
		file = a ;
	}

	public enum Options {
		//compressFormat
		  nocompress(0xfl<<32),gz(1l<<32), zip(2l<<32), bz2(3l<<32),
		//EncryptFormat
		  noEncrypt(0xfl<<36),AES(1l<<36), DES(2l<<36),
		// ScrambleFormat {
		  noScramble(0xfl<<40), FixValue(1l<<40), MersenneTwister(2l<<40),ISAACRandom(3l<<40),JDKRandomGenerator(4l<<40),SHA1PRNG(5l<<40),DRBG(6l<<40),PKCS11(7l<<40),

		// SignatureFormat {
		   noSignature(0xfl<<44), HMac(0x1l<<44),
			
		// IntegrityFormat {
		   noIntegrity(0xfl<<48), SHA256(0x1l<<48);
			
		/** access conrol manage by the node.
		 * */
		public enum accessFormat {
			  annonymous,//accessible to every one, 
			  publicAccess,//accessible to logged ones, no header encryption,no dataFileKey, but scramble is possible
			  KnownAccess,// file given to ones that know the FlatIntegrity
			  autorizedAccess,// file given to ones that are whitelisted on profil,
			  moneyAccess,// file given to ones that paid to onwer some fee for a copy,
			  privateAccess//  you should known the header key.
			  // limited protection, 
			}
	
		
		public final static long RESERVED_Value =     0x000000005A6F7562L;//"Zoub"
		private long val=RESERVED_Value;
	   
		Options(long l) {
			  this.val = l  |RESERVED_Value;			  
		}
		 public long get() {
	        return this.val;
	    }
		 /** check if feature is defined in config*/
		static public boolean is(Options feature, Options config)
			{
				long mask=feature.get();
				int i=64;
				for (;(mask&(1L<<i))==0 && (i>=0);i--);
				i=(i/4)*4;
				mask=0xf<<i;
				return (config.get()& mask)==feature.get();
			}
		 /** check if feature is defined in object*/
		 public boolean is(Options feature)
		 {
			return is( feature, this);
		 }
		
		}

	/**
	 * @param nonce the nonce to set
	 */
	public void setNonce(byte[] nonce,Options algo) {
		
		RandomGenerator rng = null;
		 SecureRandom secureRandom= null;
		 
		   int[] inonce=new int[nonce.length/4];
		   for(int i=0;i<inonce.length;i++)
			   inonce[i]=nonce[i+0]+nonce[i+1]+nonce[i+2]+nonce[i+3];
			if(algo.is(Options.MersenneTwister))
		   {
				rng =  new MersenneTwister();//229,133552 Mw/s 
				rng.setSeed(inonce);
		   }
			else if(algo.is(Options.ISAACRandom))
			{  // , , , , , , Well19937a, Well19937c, Well44497a, Well44497b, Well512a
			   rng = new ISAACRandom();//140,318304 Mw/s 
			   rng.setSeed(inonce);
			}
			else if(algo.is(Options.JDKRandomGenerator))
			{
				rng = new JDKRandomGenerator();//55,439862 Mw/s  
			    rng.setSeed(inonce);
			}
			else if(algo.is(Options.noScramble))
			{
				this.nonce =null;
			}
			else if(algo.is(Options.FixValue))
			{
				this.nonce = nonce;//save only nonce if directly used else forgot it
				index=0;
			}
		
			else
		  
			   try {
				   if(algo.is(Options.SHA1PRNG))
					secureRandom = SecureRandom.getInstance("SHA1PRNG");
				   else if(algo.is(Options.DRBG))
					secureRandom = SecureRandom.getInstance("DRBG");
				   else if(algo.is(Options.PKCS11))
					secureRandom = SecureRandom.getInstance("PKCS11");
				   else
					   throw new UnsupportedOperationException("Invalid algo for SecureRandom.");
					  secureRandom.setSeed(nonce);
			   } catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
		      
	}
	int index=0;

	@Override
	public int read() throws IOException {
		if(secureRandom!=null)
		    return file.read() ^ secureRandom.nextInt();
		else if(secureRandom!=null)
			return file.read() ^ rng.nextInt();
		else if(nonce!=null)
			return file.read() ^ nonce[index++%nonce.length];
		else
			return file.read();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
